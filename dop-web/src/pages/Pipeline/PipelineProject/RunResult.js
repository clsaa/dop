/**
 *  流水线信息展示，修改
 *  @author zhangfuli
 *
 * */

import React, {Component} from 'react';
import Axios from 'axios'
import API from '../../API'
import RunStep from '../components/RunStep'
import {NotPermission} from '../../NotFound'
import Log from '../components/Log'
import {Feedback} from "@icedesign/base/index";
const {toast} = Feedback;

export default class RunResult extends Component {
    constructor(props) {
        super(props);
        this.state = {
            stages: [],
            currentStage: 0
        }
    }

    componentWillReceiveProps(nextProps) {
        let self = this;
        if (nextProps.runs) {
            if (nextProps.runs._links.self.href !== "") {
                this.stage(nextProps.runs._links.self.href + 'nodes/').then((stages) => {
                    self.step(stages[self.state.currentStage])
                }).catch(()=>{
                    self.props.notRunning()
                    toast.show({
                        type: "error",
                        content: "流水线阶段为空，启动运行失败",
                        duration: 3000
                    });
                })
            }
        }
    }

    componentDidUpdate(prevProps, prevState) {
        if (prevState.currentStage !== this.state.currentStage) {
            this.step(this.state.stages[this.state.currentStage])
        }
    }

    //当前stage
    current(data) {
        this.setState({
            currentStage: data
        });
    }

    //获取stage
    stage(href) {
        let url = API.pipeline + "/v1/jenkins/result?path=" + href;
        let self = this;
        let stages = [];
        return new Promise((resolve, reject) => {
            Axios.get(url).then((response) => {
                if (response.status === 200) {
                    if (response.data.length === 0) {
                        reject()
                    }
                    for (let i = 0; i < response.data.length; i++) {
                        let item = response.data[i];
                        let stage = {
                            id: item.id,
                            title: item.displayName,
                            time: item.durationInMillis,
                            result: item.result,
                            disabled: item.result === 'NOT_BUILT',
                            icon: item.state === "RUNNING" ?
                                'loading' : (item.result === 'SUCCESS' ? 'success-filling' : 'delete-filling'),
                            href: item._links.steps.href,
                            steps: []
                        };
                        stages.push(stage)
                    }
                    self.setState({
                        stages
                    });
                    resolve(self.state.stages)
                }
                reject()
            })
        })
    }

    //获取step
    step(stage) {
        if (stage && stage.steps.length) {
            return
        }
        let url = API.pipeline + "/v1/jenkins/result?path=" + stage.href;
        let self = this;
        let stages = self.state.stages;
        let steps = [];
        Axios.get(url).then((response) => {
            if (response.status === 200) {
                for (let i = 0; i < response.data.length; i++) {
                    let item = response.data[i];
                    let step = {
                        id: item.id,
                        displayDescription: item.displayDescription,
                        displayName: item.displayName,
                        result: item.result,
                        time: item.durationInMillis,
                        logHref: item.actions.length === 0 ? null : item.actions[0]._links.self.href,
                        log: []
                    };
                    steps.push(step);
                }
                stage.steps = steps;
                stages.splice(self.state.currentStage, 1, stage);
                self.setState({
                    stages
                })
            }
        })
    }


    render() {
        return (
            <div className="run-detail">
                <RunStep
                    stages={this.state.stages}
                    currentStage={this.state.currentStage}
                    current={this.current.bind(this)}
                    className="run-detail-stage"
                />
                <div className="run-detail-stap">
                    {(() => {
                        let self = this;
                        if (self.state.stages.length && self.state.stages[self.state.currentStage].steps.length) {
                            return (
                                this.state.stages[self.state.currentStage].steps.map((step) => {
                                    return (
                                        <Log
                                            key={step.displayDescription}
                                            result={step.result}
                                            href={step.logHref}
                                            authorization={self.props.authorization}
                                            title={step.displayDescription ? step.displayDescription : step.displayName}
                                            prop={step.displayDescription ? step.displayName : null}
                                        />
                                    )
                                })
                            )
                        }
                    })()}
                </div>
            </div>
        );
    }
}
