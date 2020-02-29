import React, {Component} from 'react';
import './Styles.scss'
import {Icon} from "@icedesign/base";
import Axios from 'axios';
import API from '../../API'


export default class Log extends Component {
    constructor(props) {
        super(props);
        this.state = {
            content: false,
            title: this.props.title ? this.props.title : null,
            count: 1,
            logs: []
        }
    }

    componentWillMount() {
        let title = this.hide(this.state.title)
        this.setState({
            title
        })
    }
    hide(data){
        data = data.replace(/docker login -u ([^\s]*) -p ([^\s])*/, "docker login -u **** -p ****");
        data = data.replace(/http:[/][/]oauth2:(\S+)@(\S+)/, "http://oauth2:****$2");
        data = data.replace(/'Bearer \S+'/, "Bearer ****");
        return data;
    }

    clickTitle(href) {
        this.setState({
            content: !this.state.content,
            count: this.state.count + 1
        });
        if (href && this.state.count % 2) {
            let url = API.pipeline + "/v1/jenkins/result?path=" + href;
            let self = this;
            Axios.get(url).then((response) => {
                if (response.status === 200) {
                    let logs = response.data.split('\n');
                    for (let i = 0; i < logs.length; i++) {
                         logs[i] = self.hide(logs[i])
                    }
                    logs.pop();
                    self.setState({
                        logs
                    })
                }
            })
        }

    }

    render() {
        return (
            <div className="pipeline-log-accordion">
                <div className="pipeline-log-title" onClick={this.clickTitle.bind(this, this.props.href)}>
                    {(() => {
                        if (this.props.result === 'SUCCESS') {
                            return (
                                <div className="div-icon success">
                                    <Icon type="select" className="icon" size="xxs"/>
                                </div>
                            )
                        } else if (this.props.result === 'FAILURE') {
                            return (
                                <div className="div-icon fail">
                                    <Icon type="close" className="icon" size="xxs"/>
                                </div>
                            )
                        } else {
                            return (
                                <div className="div-icon loading">
                                    <Icon type="loading" className="icon" size="xxs"/>
                                </div>
                            )
                        }
                    })()}

                    {
                        this.state.content ?
                            <div className="div-icon2">
                                <Icon type="arrow-down" className="icon" size="xs"/>
                            </div> :
                            <div className="div-icon2">
                                <Icon type="arrow-right" className="icon" size="xs"/>
                            </div>
                    }


                    <div className="process">{this.state.title}</div>
                    <div className="prop">{this.props.prop ? "--" + this.props.prop : null}</div>
                </div>

                {this.state.content ?
                    (<div className="content">
                        <pre className="pre">
                            <ul className="ol">
                                <li className="li">
                                    {this.state.logs.map((log, index) => {
                                        return (
                                            <div key={index} className="li-wrap">
                                                <span className="idx">{index}</span>
                                                <span className="shell">
                                                    {log}
                                                </span>
                                            </div>
                                        )
                                    })}

                                </li>
                            </ul>
                        </pre>
                    </div>) : null
                }

            </div>
        )
    }
}
