/**
 *  流水线信息展示，修改
 *  @author zhangfuli
 *
 * */
import React, {Component} from 'react';
import {Button, Icon, Loading, Feedback, Progress} from '@icedesign/base';
import Axios from 'axios';
import API from '../../API';
import RunResult from './RunResult'
import {NotPermission} from '../../NotFound'
import {injectIntl, FormattedMessage} from 'react-intl';
import './PipelineProject.scss'

const {toast} = Feedback;

class PipelineProject extends Component {
    constructor(props) {
        super(props);
        this.state = {
            visible: false,
            pipelineId: this.props.match.params.id,
            pipeline: {},
            runs: {
                _links: {
                    self: {
                        href: ""
                    }
                }
            },
            queue: [],
            time: "",
            resultStatus: "NOTBUILD",
            notRunning: null
        }
    }

    componentDidMount() {
        let self = this;
        self.getPipelineInfo();
        let time = setInterval(() => {
            this.getRuns().then((data) => {
                self.setState({
                    runs: data,
                    visible: false
                })
            })
        }, 10000);
        self.setState({
            time: time
        });
    }

    projectLoading() {
        let progress = Math.round(Math.random() * 50) + 50;
        return (
            <div className="project-progress">
                <FormattedMessage
                    id="pipeline.project.loading"
                    defaultMessage="正在加载..."
                />
                <Progress percent={progress} size="large" showInfo={false}/>
            </div>
        )
    }

    getPipelineInfo() {
        this.setState({
            visible: true
        });
        let url = API.pipeline + '/v1/pipeline/' + this.state.pipelineId;
        let self = this;

        Axios.get(url).then((response) => {
            if (response.status === 200) {
                self.setState({
                    pipeline: response.data
                })
            }
        })
    }

    getRuns() {
        this.setState({
            visible: true
        });
        let url = API.pipeline + '/v1/jenkins/runs?id=' + this.state.pipelineId;
        let self = this;
        return new Promise((resolve, reject) => {
            Axios.get(url).then((response) => {
                if (response.status === 200) {
                    if (response.data.length === 0) {
                        toast.show({
                            type: "prompt",
                            content: self.props.intl.messages["pipeline.project.notRun"],
                            duration: 3000
                        });
                        self.setState({
                            notRunning: true,
                            visible: false
                        });
                        resolve(response.data[0]);
                        self.setResult();
                        self.clear();
                    } else {
                        self.setState({
                            notRunning: false
                        });
                        console.log(response.data[0])
                        if (response.data[0].state === 'FINISHED') {
                            self.clear();
                            if (self.state.resultStatus === "BUILD") {
                                self.setResult();
                            }
                        }
                        resolve(response.data[0]);
                    }
                }
                reject()
            }).catch((error) => {
                let self = this;
                if (error.response.status === 500 && error.response.data.message === "404 Not Found") {
                    toast.show({
                        type: "prompt",
                        content: "该流水线尚未运行",
                        duration: 3000
                    });
                }
                self.setState({
                    visible: false
                });
                self.clear();
            })
        })
    }

    buildPipeline() {
        let self = this;
        self.setState({
            visible: true,
            resultStatus: "BUILD",
            notRunning: false
        });
        let url = API.pipeline + '/v1/jenkins/build/' + this.state.pipelineId;
        Axios.post(url).then((response) => {
            if (response.status === 200) {
                let time = setInterval(() => {
                    this.getRuns().then((data) => {
                        self.setState({
                            runs: data,
                            visible: false
                        })
                    })
                }, 10000);
                self.setState({
                    runs: response.data,
                    time: time
                });
            }
        }).catch((error) => {
            toast.show({
                type: "error",
                content: "启动运行失败",
                duration: 3000
            });
            self.setState({
                visible: false,
                resultStatus: "NOTBUILD"
            });
        })
    }

    clear() {
        clearInterval(this.state.time);
    }

    setResult() {
        this.setState({
            resultStatus: "RUN"
        });
        let url = API.pipeline + '/v1/resultOutput/notify/' + this.state.pipelineId;
        Axios.post(url).then((response) => {
        }).catch(() => {
            toast.show({
                type: "error",
                content: "此次执行信息丢失",
                duration: 3000
            });
        })
    }

    removeByIdSQL(id) {
        let url = API.pipeline + '/v1/delete/' + id;
        let self = this;
        Axios.put(url).then((response) => {
            if (response.status === 200) {
                toast.show({
                    type: "success",
                    content: "删除成功",
                    duration: 1000
                });
                self.props.history.push('/pipeline')
            }
        })
    }
    notRunning(){
        this.setState({
            notRunning: true
        })
    }
    editPipeline() {
        this.props.history.push("/pipeline/edit/" + this.state.pipelineId)
    }

    deletePipeline() {
        let url = API.pipeline + '/v1/jenkins/' + this.state.pipelineId;
        let self = this;
        self.setState({
            visible: true
        });
        Axios({
            method: 'delete',
            url: url,
        }).then((response) => {
            if (response.status === 200) {
                self.removeByIdSQL(this.state.pipelineId);
            } else {
                toast.show({
                    type: "error",
                    content: "删除失败",
                    duration: 1000
                });
            }
        });
    }

    render() {
        return (
            <div className="project-body">
                <div className="operate">
                    <Button type="primary" className="button" onClick={this.buildPipeline.bind(this)}>
                        <Icon type="play"/>
                        <FormattedMessage
                            id="pipeline.project.runPipeline"
                            defaultMessage="运行流水线"
                        />
                    </Button>
                    <Button type="normal" className="button" onClick={this.editPipeline.bind(this)}>
                        <Icon type="edit"/>
                        <FormattedMessage
                            id="pipeline.project.editPipeline"
                            defaultMessage=" 编辑流水线"
                        />
                    </Button>
                    <Button type="secondary" shape="warning" className="button"
                            onClick={this.deletePipeline.bind(this)}>
                        <Icon type="ashbin"/>
                        <FormattedMessage
                            id="pipeline.project.deletePipeline"
                            defaultMessage="删除流水线"
                        />
                    </Button>
                </div>
                <Loading tip={this.projectLoading()} visible={this.state.visible} className="next-loading my-loading">
                    {(() => {
                        if (this.state.notRunning) {
                            return <NotPermission/>
                        } else {
                            return (
                                <RunResult
                                    runs={this.state.runs}
                                    notRunning={this.notRunning.bind(this)}
                                />
                            )
                        }
                    })()}
                </Loading>
            </div>

        );
    }
}
export default injectIntl(PipelineProject)
