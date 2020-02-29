import React, {Component} from 'react';
import {Step, Icon} from '@icedesign/base';
import PipelineInfoStep from './PipelineInfoStep'
import {injectIntl} from "react-intl";

class PipelineInfoStage extends Component {
    constructor(props) {
        super(props)
        this.state = {
            currentStage: 0,
            stages: [],
        }
    }

    componentWillMount() {
        let self = this;
        this.setState({
            stages: self.props.stages
        })
    }
    componentWillReceiveProps(nextProps){
        this.setState({
            stages: nextProps.stages,
        });
    }
    componentDidUpdate(prevProps, prevState) {
        if (prevState.stages !== this.state.stages) {
            this.props.onChange(this.state.stages)
        }
    }

    /**
     * stage数字显示
     * */
    stepItemRender(index) {
        return index + 1
    }

    stepItemAdd() {
        return (
            <Icon type="add"/>
        );
    }

    title(value, index) {
        return  this.state.currentStage === index?(
            <div>
                <Icon type="delete-filling" size="xs" className="closeStage" onClick={this.close.bind(this, index)}/>
                <span>{value}</span>
                <Icon type="delete-filling" size="xs" className="addStage" onClick={this.addStageMid.bind(this, index)}/>
            </div>
        ):(
            <div>
                <span>{value}</span>
            </div>
        )
    }
    close(index) {
        let stages = this.state.stages;
        stages.splice(index, 1);
        this.setState({
            stages: stages,
            currentStage: this.state.currentStage===0?0:this.state.currentStage - 1
        });
    }


    /**
     * 切换stage, 将新信息放到currentStage
     * */
    changeStage(currentStage) {
        this.setState({
            currentStage
        });
    }
    addStageMid(index){
        let stages = this.state.stages;
        let newStage = {
            name: this.props.intl.messages["pipeline.info.stage.name"],
            steps: []
        };
        stages.splice(index+1, 0, newStage);
        this.setState({
            stages
        })
    }
    addStage() {
        let newStage = {
            name: this.props.intl.messages["pipeline.info.stage.name"],
            steps: []
        };
        this.state.stages.push(newStage);
        this.setState({
            currentStage: this.state.stages.length - 1
        })
    }

    onChangeApp(value){
        this.props.onChangeApp(value)
    }
    onSelectEnv(value){
        this.props.onSelectEnv(value)
    }

    onChangeDockerUserName(value){
        this.props.onChangeDockerUserName(value)
    }
    onChangeRepository(value){
        this.props.onChangeRepository(value)
    }
    step(value) {
        let stages = this.state.stages;
        stages[this.state.currentStage] = value;
        this.setState({
            stages
        });
        this.props.onChange(stages, this.state.currentStage)
    }
    render() {
        return (
            <div className="pipeline-info-stage-step">
                <Step className="pipeline-info-stage" type="circle" animation={true}
                      current={this.state.currentStage}
                >
                    {this.state.stages.map((stage, index) => {
                        return (
                            <Step.Item key={index} title={this.title(stage.name, index)}
                                       itemRender={this.stepItemRender}
                                       className="pipeline-info-stage-stepItem"
                                       onClick={this.changeStage.bind(this, index)}
                            />
                        )
                    })}
                    <Step.Item title={this.props.intl.messages["pipeline.info.stage.add"]}
                               itemRender={this.stepItemAdd.bind(this)}
                               className="pipeline-info-stage-stepItem"
                               onClick={this.addStage.bind(this, this.state.stages.length)}
                    />
                </Step>

                <PipelineInfoStep
                    stage={this.state.stages[this.state.currentStage]}
                    appId = {this.props.appId}
                    appEnvId = {this.props.appEnvId}
                    onChange={this.step.bind(this)}
                    onChangeApp={this.onChangeApp.bind(this)}
                    onSelectEnv = {this.onSelectEnv.bind(this)}
                    currentStage={this.state.currentStage}

                    dockerUserName={this.props.dockerUserName}
                    onChangeDockerUserName={this.onChangeDockerUserName.bind(this)}
                    repository={this.props.repository}
                    onChangeRepository={this.onChangeRepository.bind(this)}
                />
            </div>
        )
    }
}
export default injectIntl(PipelineInfoStage)
