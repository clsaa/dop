import React, {Component} from 'react';
import {Form, Select, Input, Field, Loading} from "@icedesign/base";
import API from '../../../API'
import Axios from 'axios'
import '../Styles.scss'
import {injectIntl} from "react-intl";
import {Feedback} from "@icedesign/base/index";

const FormItem = Form.Item;
const {Combobox} = Select;
const {toast} = Feedback;


class Pull extends Component {
    constructor(props) {
        super(props);
        this.field = new Field(this);
        this.state = {
            applications: [],
            visible: false,
            selectedApp: null,
            gitUrl: this.props.gitUrl,
            appId: this.props.appId
        }
    }

    componentWillMount() {
        this.getApplication();
    }

    getApplication() {
        this.setState({
            visible: true
        });
        return new Promise((resolve, reject) => {
            let url = API.application + "/app?ouser=" + window.sessionStorage.getItem('user-id');
            let self = this;
            let applications = self.state.applications;
            Axios.get(url).then((response) => {
                if (response.status === 200) {
                    this.setState({
                        visible: false
                    });
                    for (let i = 0; i < response.data.length; i++) {
                        let application = {
                            label: response.data[i].title,
                            value: response.data[i].id
                        };
                        if (self.props.appId === response.data[i].id) {
                            self.selectApplication(response.data[i].id);
                            self.setState({
                                selectedApp: response.data[i].title
                            });

                        }
                        applications.push(application)
                    }
                    self.setState({
                        applications: applications
                    });
                    resolve()
                }
            }).catch(() => {
                toast.show({
                    type: "error",
                    content: self.props.intl.messages["pipeline.info.step.pull.apply.error"],
                    duration: 3000
                });
                reject()
            })
        })

    }

    getGit(value) {
        this.setState({
            visible: true
        });
        let url = API.application + "/app/" + value + "/urlInfo";
        let self = this;
        Axios.get(url).then((response) => {
            if (response.status === 200) {
                self.setState({
                    gitUrl: response.data.warehouseUrl
                });
                self.props.onChange(response.data.warehouseUrl);
                this.setState({
                    visible: false
                });
            }
        }).catch(() => {
            this.setState({
                visible: false
            });
        })
    }

    buildMavenGit(value) {
        this.props.onChange(value)
    }

    selectApplication(value) {
        this.props.onChangeApp(value);
        this.setState({
            selectedApp: value
        });
        this.getGit(value)
    }

    render() {
        const formItemLayout = {
            labelCol: {
                span: 10
            },
            wrapperCol: {
                span: 10
            }
        };
        return (
            <Loading shape="fusion-reactor" visible={this.state.visible}>
                <h3 className="chosen-task-detail-title">{this.props.intl.messages["pipeline.info.step.pull.title"]}</h3>
                <Form
                    labelAlign="left"
                    labelTextAlign="left"
                >
                    <FormItem
                        label={this.props.intl.messages["pipeline.info.step.pull.apply"] + "："}
                        {...formItemLayout}
                    >
                        <Combobox
                            fillProps= "label"
                            onChange={this.selectApplication.bind(this)}
                            dataSource={this.state.applications}
                            value={this.state.selectedApp ? this.state.selectedApp : this.props.intl.messages["pipeline.info.apply.placeholder"]}
                            style={{'width': '250px'}}
                        >
                        </Combobox>
                    </FormItem>
                    <FormItem
                        label={this.props.intl.messages["pipeline.info.step.pull.git"] + "："}
                        required
                        {...formItemLayout}
                    >
                        <Input
                            onChange={this.buildMavenGit.bind(this)}
                            disabled
                            style={{'width': '250px'}}
                            value={this.state.gitUrl}
                        />
                    </FormItem>
                </Form>
            </Loading>
        )
    }
}

export default injectIntl(Pull)
