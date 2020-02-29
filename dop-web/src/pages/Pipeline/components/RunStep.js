import React, {Component} from 'react';
import {Step} from '@icedesign/base';
import './Styles.scss'

export default class RunStep extends Component {

    current(index) {
        this.props.current(index)
    }

    render() {
        return (
            <Step current={this.props.currentStage} type="dot" className={this.props.className + " step"}>
                {
                    this.props.stages.map((item, index) => {
                        return (
                            <Step.Item
                                className="stepItem"
                                {...item}
                                key={index}
                                onClick={this.current.bind(this, index)}
                            />
                        )
                    })
                }
            </Step>
        )
    }
}
