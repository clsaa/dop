import React, {Component} from 'react';
import '../Styles.scss'
import {Form, Select, Loading} from "@icedesign/base";
import Axios from "axios/index";
import API from "../../../API";
import {injectIntl} from "react-intl";
import {Feedback} from "@icedesign/base/index";

const {toast} = Feedback;
const FormItem = Form.Item;
const {Combobox} = Select;


class DockerImage extends Component {
    constructor(props) {
        super(props);
        this.state = {
            visible: false,
            applications: [],
            selectedApp: null,
            environments: [],
            selectedEnv: null,
            dockerUser: [],
            repositories: [],
            repositoryVersions: []
        }

    }

    componentWillMount() {
        this.getApplication();
    }

    getApplication() {
        this.setState({
            applications:[],
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
                            self.setState({
                                selectedApp: response.data[i].title
                            });
                            self.selectApplication(response.data[i].id);
                        }
                        applications.push(application)
                    }
                    self.setState({
                        applications: [...new Set(applications)]
                    });
                    resolve()
                }
            }).catch(() => {
                toast.show({
                    type: "error",
                    content: self.props.intl.messages["pipeline.info.step.docker.apply.error"],
                    duration: 3000
                });
                reject()
            })
        })

    }

    selectApplication(value) {
        this.setState({
            selectedApp: value
        });
        this.props.onChangeApp(value);

        this.getEnv(value);
        this.getRepository(value);
        this.getDockerUser()
    }


    getEnv(data) {
        this.setState({
            visible: true
        });
        let url = API.application + "/app/" + data + "/allEnv";
        let self = this;
        let environments = [];

        Axios.get(url).then((response) => {
            if (response.status === 200) {
                for (let i = 0; i < response.data.length; i++) {
                    let environment = {
                        label: response.data[i].title,
                        value: response.data[i].id
                    };
                    environments.push(environment);
                    if (self.props.selectEnvId === response.data[i].id) {
                        self.selectEnv(response.data[i].id)
                        self.setState({
                            selectedEnv: response.data[i].title
                        });
                    }
                    if (self.props.appEnvId === response.data[i].id) {
                        self.selectEnv(response.data[i].id)
                        self.setState({
                            selectedEnv: response.data[i].title
                        });
                    }
                }
                self.setState({
                    environments:[...new Set(environments)],
                    visible: false
                });
            }
        }).catch(()=>{
            this.setState({
                visible: false
            });
        })
    }

    getDockerUser() {
        this.setState({
            visible: true
        });
        let url = API.user + "/v1/users/" + window.sessionStorage.getItem("user-id") + "/credential?type=DOP_INNER_HARBOR_LOGIN_EMAIL";
        let self = this;
        let dockerUser = [];
        Axios.get(url).then((response) => {
            if (response.status === 200) {
                let user = {
                    label: response.data.identifier,
                    value: response.data.id,
                    id: response.data.id,
                    credential: response.data.credential,
                    identifier: response.data.identifier,
                };
                dockerUser.push(user);
                self.setState({
                    dockerUser:[...new Set(dockerUser)],
                    visible: false
                })
            }
        }).catch(()=>{
            this.setState({
                visible: false
            });
        })
    }

    selectEnv(value) {
        this.setState({
            selectedEnv: value
        });
        this.props.onSelectEnv(value)
    }

    getRepository(data) {
        this.setState({
            visible: true
        });
        let url = API.application + "/app/" + data + "/urlInfo";
        let self = this;
        let repositories = [];
        Axios.get(url).then((response) => {
            if (response.status === 200) {
                let repository = {
                    value: response.data.imageUrl,
                    label: response.data.imageUrl,
                    key: self.state.repositories.length
                };
                repositories.push(repository);
                self.setState({
                    repositories:[...new Set(repositories)],
                    visible: false
                });
            }
        }).catch(()=>{
            this.setState({
                visible: false
            });
        })
    }

    selectDocker(value) {
        let self = this;
        let dockerUser = this.state.dockerUser;
        for (let i = 0; i < dockerUser.length; i++) {
            if (value === dockerUser[i].id) {
                self.props.onUserNameChange(dockerUser[i].identifier);
                self.props.onDockerPasswordChange(dockerUser[i].credential)
                console.log(dockerUser[i].identifier)
            }
        }
    }

    selectRepository(value) {
        this.props.onRepositoryChange(value)
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
                <h3 className="chosen-task-detail-title">{this.props.intl.messages["pipeline.info.step.docker.title"]}</h3>
                <Form
                    labelAlign="left"
                    labelTextAlign="left"
                >
                    <FormItem
                        label={this.props.intl.messages["pipeline.info.step.docker.apply"] + ": "}
                        {...formItemLayout}
                    >
                        <Combobox
                            key="0"
                            onChange={this.selectApplication.bind(this)}
                            dataSource={this.state.applications}
                            value={this.state.selectedApp ? this.state.selectedApp : this.props.intl.messages["pipeline.info.apply.placeholder"]}
                            fillProps="label"
                            style={{'width': '200px'}}
                        />
                    </FormItem>
                    <FormItem
                        label={this.props.intl.messages["pipeline.info.step.docker.environment"] + ": "}
                        {...formItemLayout}
                        required
                    >
                        <Combobox
                            key="1"
                            fillProps="label"
                            style={{'width': '200px'}}
                            onChange={this.selectEnv.bind(this)}
                            placeholder={this.state.selectedEnv ? this.state.selectedEnv : this.props.intl.messages["pipeline.info.step.docker.environment.placeholder"]}
                            disabled={!this.state.selectedApp}
                            dataSource={this.state.environments}
                        />
                    </FormItem>
                    <FormItem
                        label="DockerUserName："
                        {...formItemLayout}
                        required
                    >
                        <Combobox
                            key="2"
                            fillProps="label"
                            dataSource={this.state.dockerUser}
                            style={{'width': '200px'}}
                            disabled={!this.state.selectedApp}
                            placeholder={this.props.dockerUserName?this.props.dockerUserName:this.props.intl.messages["pipeline.info.step.docker.dockerUser.placeholder"]}
                            onChange={this.selectDocker.bind(this)}
                        />

                    </FormItem>
                    <FormItem
                        label="Repository："
                        {...formItemLayout}
                        required
                    >
                        <Combobox
                            key="3"
                            fillProps="label"
                            style={{'width': '200px'}}
                            onChange={this.selectRepository.bind(this)}
                            placeholder={this.props.repository ? this.props.repository : this.props.intl.messages["pipeline.info.step.docker.repository.placeholder"]}
                            disabled={!this.state.selectedEnv}
                            dataSource={this.state.repositories}
                        />
                    </FormItem>
                </Form>
            </Loading>
        )
    }
}

export default injectIntl(DockerImage)
