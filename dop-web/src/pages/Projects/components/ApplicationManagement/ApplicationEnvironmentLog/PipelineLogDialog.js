import {Dialog, Loading} from "@icedesign/base";
import React, {Component} from 'react';
import API from "../../../../API.js"
import Axios from "axios";
import {injectIntl} from "react-intl";
import "./PipelineLogDialog.scss"

class PipelineLogDialog extends Component {
    constructor(props) {
        super(props);
        this.state = {
            logData: null,
            isLoading: true,
            runningId: props.runningId,
            visible: false
        }
    }


    getData() {
        let url = API.application + "/pipelineLog/" + this.state.runningId
        let _this = this
        Axios.get(url)
            .then((response) => {
                console.log(response)
                _this.setState({
                    isLoading: false,
                    logData: response.data.result
                })
            })
            .catch((response) => {
                console.log(response)
                _this.setState({
                    isLoading: false
                })
            })

    }

    toggleVisible() {
        console.log("toogle")
        this.getData()
        this.setState({
            visible: true
        })
    }

    onClose() {
        this.setState({
            visible: false
        })
    }

    render() {

        return (
            <span>
             <div className="runningId" onClick={this.toggleVisible.bind(this)}>
                 {this.state.runningId}
             </div>
 <Dialog

     shouldUpdatePosition
     className='pipeline-log-dialog'
     footer={(
         <div/>
     )}
     onClose={this.onClose.bind(this)}
     visible={this.state.visible}>
            <Loading visible={this.state.isLoading} shape="dot-circle" color="#2077FF">
            <pre className="pre">{this.state.isLoading ? "" : this.state.logData}</pre>
    </Loading>
    </Dialog>
            </span>

        )
    }

}

export default injectIntl(PipelineLogDialog)