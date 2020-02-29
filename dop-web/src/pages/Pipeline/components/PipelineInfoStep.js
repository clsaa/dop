import React, {Component} from 'react';
import {FormBinderWrapper, FormBinder, FormError} from '@icedesign/form-binder';
import {Input, Select, Icon} from '@icedesign/base';
import IceLabel from '@icedesign/label';
import './Styles.scss';

import Pull from './chosenSteps/Pull'
import Maven from './chosenSteps/Maven'
import Node from './chosenSteps/Node'
import Djanggo from './chosenSteps/Django'
import DockerImage from './chosenSteps/DockerImage'
import Shell from './chosenSteps/Shell'
import Deployment from './chosenSteps/Deployment'
import {injectIntl} from "react-intl";

const {Combobox} = Select;

class PipelineInfoStep extends Component {
    constructor(props) {
        super(props);
        this.state = {
            stage: this.props.stage,
            availableSteps: [
                this.props.intl.messages['pipeline.info.step.pull'],
                this.props.intl.messages['pipeline.info.step.maven'],
                this.props.intl.messages['pipeline.info.step.node'],
                this.props.intl.messages['pipeline.info.step.djanggo'],
                this.props.intl.messages['pipeline.info.step.buildDocker'],
                this.props.intl.messages['pipeline.info.step.pushDocker'],
                this.props.intl.messages['pipeline.info.step.custom'],
                this.props.intl.messages['pipeline.info.step.deploy'],
            ],
            steps: [],
            currentStep: null,
            selectEnvId: null,
            chosenStep: {
                taskName: null
            }
        }
    }

    componentWillReceiveProps(nextProps) {
        let self = this;
        if (nextProps.currentStage !== undefined && this.props.currentStage !== nextProps.currentStage) {
            let chosenStep = {
                taskName: null
            };
            if (nextProps.stage) {
                if (nextProps.stage.steps.length) {
                    chosenStep = nextProps.stage.steps[0]
                }
            }
            self.setState({chosenStep})
        }
        if (nextProps.stage) {
            self.setState({
                stage: nextProps.stage,
            })
        }
    }

    formChange(value) {
        this.props.onChange(value)
    };

    selectStep(value) {
        let index = this.state.availableSteps.indexOf(value);
        let newTask = {
            taskName: index,
            gitUrl: "",
            dockerUserName: "",
            dockerPassword: "",
            repository: "",
            repositoryVersion: "",
            shell: "",
            deploy: "",
            ip: "",
            token: ""
        };
        let chosenStep = newTask;
        let steps = this.state.stage.steps;
        steps.push(newTask);
        let stage = Object.assign({}, this.state.stage, {steps: steps});
        this.setState({
            stage,
            chosenStep
        });
    }

    /**
     *  关掉所选task  label
     * */
    closeTask(index) {
        let stage = this.state.stage;
        stage.steps.splice(index, 1);
        let chosenStep = {
            taskName: null
        };
        this.setState({
            stage,
            chosenStep
        });
    }

    /**
     *  点击编辑task
     * */
    editTask(chosenStep) {
        this.setState({
            chosenStep
        })
    }

    onChangeApp(value) {
        this.props.onChangeApp(value)
    }

    onSelectEnv(value) {
        this.setState({
            selectEnvId: value
        });
        this.props.onSelectEnv(value)
    }

    gitUrl(value) {
        let findIndex = this.state.stage.steps.findIndex((item) => {
            return item.taskName === this.state.chosenStep.taskName
        });
        let stage = this.state.stage;
        stage.steps[findIndex].gitUrl = value;
        this.setState({
            stage
        });
        this.props.onChange(this.state.stage)
    }

    buildDockerUserName(value) {
        console.log(value)
        let findIndex = this.state.stage.steps.findIndex((item) => {
            return item.taskName === this.state.chosenStep.taskName
        });
        let stage = this.state.stage;
        stage.steps[findIndex].dockerUserName = value;
        this.setState({
            stage
        });
        this.props.onChangeDockerUserName(value)
        this.props.onChange(this.state.stage)

    }

    buildRepository(value) {
        let findIndex = this.state.stage.steps.findIndex((item) => {
            return item.taskName === this.state.chosenStep.taskName
        });
        let stage = this.state.stage;
        stage.steps[findIndex].repository = value;
        this.setState({
            stage
        });
        this.props.onChangeRepository(value)
        this.props.onChange(this.state.stage)
    }


    buildDockerPassword(value) {
        let findIndex = this.state.stage.steps.findIndex((item) => {
            return item.taskName === this.state.chosenStep.taskName
        });
        let stage = this.state.stage;
        stage.steps[findIndex].dockerPassword = value;
        this.setState({
            stage
        });
        this.props.onChange(this.state.stage)
    }

    shell(value) {
        let findIndex = this.state.stage.steps.findIndex((item) => {
            return item.taskName === this.state.chosenStep.taskName
        });
        let stage = this.state.stage;
        stage.steps[findIndex].shell = value;
        this.setState({
            stage
        });
        this.props.onChange(this.state.stage)
    }

    render() {
        return (
            <div>
                {(() => {
                    if (this.state.stage) {
                        return (
                            <FormBinderWrapper
                                value={this.state.stage}
                                onChange={this.formChange.bind(this)}
                                ref="form"
                            >
                                <div className="pipeline-info-step">
                                    <h3 className="header">{this.props.intl.messages["pipeline.info.stage.title"]}</h3>
                                    <div>
                                        <span
                                            className="label">{this.props.intl.messages["pipeline.info.stage.name.title"]}: </span>
                                        <FormBinder name="name" required
                                                    message={this.props.intl.messages["pipeline.info.stage.name"]}>
                                            <Input
                                                value={this.state.stage.name}
                                            />
                                        </FormBinder>
                                        <FormError className="form-item-error" name="name"/>
                                    </div>
                                    <div className="task">
                                    <span className="task-label-set">
                                        {this.props.intl.messages["pipeline.info.step.title"]}:
                                        <p>*{this.props.intl.messages["pipeline.info.step.tip"]}</p>
                                    </span>
                                        <div className="choose-task">
                                            <Combobox
                                                filterLocal={false}
                                                placeholder={this.props.intl.messages["pipeline.info.step.placeholder"]}
                                                onChange={this.selectStep.bind(this)}
                                                dataSource={this.state.availableSteps}
                                            />
                                            {(() => {
                                                if (this.state.stage && this.state.stage.steps) {
                                                    return (this.state.stage.steps.map((item, index) => {
                                                        let chosenStyle = item.taskName === this.state.chosenStep.taskName ? "chosen task-label" : "task-label";
                                                        return (
                                                            <div className="chosen-task" key={index}>
                                                                <IceLabel className={chosenStyle}>
                                                        <span
                                                            onClick={this.editTask.bind(this, item)}
                                                        >{this.state.availableSteps[item.taskName]}</span>
                                                                    <Icon type="close" size="xs" className="close"
                                                                          onClick={this.closeTask.bind(this, index)}
                                                                    />
                                                                </IceLabel>
                                                            </div>
                                                        )
                                                    }))
                                                }
                                            })()}
                                        </div>
                                        {(() => {
                                            if (this.state.chosenStep.taskName !== undefined) {
                                                return (
                                                    <div>
                                                        {(() => {
                                                            switch (this.state.chosenStep.taskName) {
                                                                case 0:
                                                                    return (
                                                                        <div className="chosen-task-detail">
                                                                            <Pull
                                                                                onChange={this.gitUrl.bind(this)}
                                                                                onChangeApp={this.onChangeApp.bind(this)}
                                                                                gitUrl={this.state.chosenStep.gitUrl}
                                                                                appId={this.props.appId}
                                                                            />
                                                                        </div>
                                                                    );
                                                                case 1:
                                                                    return (
                                                                        <div className="chosen-task-detail">
                                                                            <Maven/>
                                                                        </div>
                                                                    );
                                                                case 2:
                                                                    return (
                                                                        <div className="chosen-task-detail">
                                                                            <Node/>
                                                                        </div>
                                                                    );
                                                                case 3:
                                                                    return (
                                                                        <div className="chosen-task-detail">
                                                                            <Djanggo/>
                                                                        </div>
                                                                    );
                                                                case 4:
                                                                    return (
                                                                        <div className="chosen-task-detail">
                                                                            <DockerImage
                                                                                appId={this.props.appId}
                                                                                appEnvId={this.props.appEnvId}
                                                                                onChangeApp={this.onChangeApp.bind(this)}
                                                                                onSelectEnv={this.onSelectEnv.bind(this)}
                                                                                onUserNameChange={this.buildDockerUserName.bind(this)}
                                                                                onDockerPasswordChange={this.buildDockerPassword.bind(this)}
                                                                                onRepositoryChange={this.buildRepository.bind(this)}
                                                                                dockerUserName={this.state.chosenStep.dockerUserName}
                                                                                repository={this.state.chosenStep.repository}
                                                                                selectEnvId={this.state.selectEnvId}
                                                                            />
                                                                        </div>
                                                                    );
                                                                case 5:
                                                                    return (
                                                                        <div className="chosen-task-detail">
                                                                            <DockerImage
                                                                                appId={this.props.appId}
                                                                                appEnvId={this.props.appEnvId}
                                                                                onChangeApp={this.onChangeApp.bind(this)}
                                                                                onSelectEnv={this.onSelectEnv.bind(this)}
                                                                                onUserNameChange={this.buildDockerUserName.bind(this)}
                                                                                onDockerPasswordChange={this.buildDockerPassword.bind(this)}
                                                                                onRepositoryChange={this.buildRepository.bind(this)}
                                                                                dockerUserName={this.state.chosenStep.dockerUserName}
                                                                                repository={this.state.chosenStep.repository}
                                                                                selectEnvId={this.state.selectEnvId}
                                                                            />
                                                                        </div>
                                                                    );
                                                                case 6:
                                                                    return (
                                                                        <div className="chosen-task-detail">
                                                                            <Shell
                                                                                onShellChange={this.shell.bind(this)}
                                                                                shell={this.state.chosenStep.shell}
                                                                            />
                                                                        </div>
                                                                    );
                                                                case 7:
                                                                    return (
                                                                        <div className="chosen-task-detail">
                                                                            <Deployment/>
                                                                        </div>
                                                                    );
                                                                default:
                                                            }
                                                        })()}
                                                    </div>
                                                )
                                            }
                                        })()}
                                    </div>

                                </div>
                            </FormBinderWrapper>
                        )
                    }
                })()}
            </div>
        )
    }
}

export default injectIntl(PipelineInfoStep)
