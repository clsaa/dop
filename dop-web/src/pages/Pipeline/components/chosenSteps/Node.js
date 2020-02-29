import React, {Component} from 'react';
import '../Styles.scss'
import {injectIntl} from "react-intl";


class Node extends Component{
    render(){
        return (
            <div>
                <h3 className="chosen-task-detail-title">{this.props.intl.messages["pipeline.info.step.node.title"]}</h3>
                <div
                    className="chosen-task-detail-body">
                    {this.props.intl.messages["pipeline.info.step.node.tip"]} <br/><br/>
                    {this.props.intl.messages["pipeline.info.step.node.npm.version"]}<br/>
                    {this.props.intl.messages["pipeline.info.step.node.node.version"]}<br/>
                </div>
            </div>
        )
    }
}
export default injectIntl(Node)
