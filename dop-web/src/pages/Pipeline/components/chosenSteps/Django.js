import React, {Component} from 'react';
import '../Styles.scss'
import {injectIntl} from "react-intl";


class Django extends Component{
    render(){
        return (
            <div>
                <h3 className="chosen-task-detail-title">{this.props.intl.messages["pipeline.info.step.django.title"]}</h3>
                <div
                    className="chosen-task-detail-body">
                    {this.props.intl.messages["pipeline.info.step.django.tip"]}<br/><br/>
                    {this.props.intl.messages["pipeline.info.step.django.python"]}<br/>
                    {this.props.intl.messages["pipeline.info.step.django.pip"]}<br/>
                </div>
            </div>
        )
    }
}

export default injectIntl(Django)
