import {Button, Dialog} from "@icedesign/base";


import React, {Component} from 'react';

import "./CreateApplicationEnvironment.scss"
import {injectIntl} from "react-intl";

import CreateApplicationEnvironmentForm from './CreateApplicationEnvironmentForm'


/**
 *    创建应用环境的弹窗
 *
 * */
class CreateApplicationEnvironmentDialog extends Component {


    onClose = () => {
        this.setState({
            visible: false
        });
    };

    onOk = () => {
        this.setState({
            isSubmit: true
            // visible: false
        });
    }


    onOpen = () => {
        this.setState({
            visible: true
        });
    };
    onCreateDialogClose = () => {
        this.setState({
            createDialogVisible: false
        });
    };

    constructor(props) {
        super(props);

        this.state = {
            //是否已经提交
            isSubmit: false,
            footerAlign: "center",
            visible: false,
            createDialogVisible: false,
            refreshApplicationEnvironmentList: props.refreshApplicationEnvironmentList,
            appId: props.appId
        }
    };

    /**
     *    回调函数传给子组件表单用于创建完成后修改提交状态和关闭弹窗
     *
     * */
    finished() {
        this.setState({
            visible: false,
            createDialogVisible: true,
            isSubmit: false
        })

        this.state.refreshApplicationEnvironmentList();
        console.log("finished");
    }

    render() {
        return (
            <span>
                <Button onClick={this.onOpen} type="primary">
                    {this.props.intl.messages['projects.button.createEnv']}
        </Button>
        <Dialog
            visible={this.state.visible}
            onOk={this.onOk}
            onCancel={this.onClose}
            onClose={this.onClose}
            title={this.props.intl.messages['projects.button.createEnv']}
            className="create-app-env-dialog"
            footerAlign={this.state.footerAlign}
            locale={{
                ok: this.props.intl.messages["projects.button.confirm"],
                cancel: this.props.intl.messages["projects.button.cancel"]
            }}
        >
          <CreateApplicationEnvironmentForm isSubmit={this.state.isSubmit} finished={this.finished.bind(this)}
                                            appId={this.state.appId}/>
        </Dialog>

<Dialog visible={this.state.createDialogVisible}
        onOk={this.onCreateDialogClose}
        onCancel={this.onCreateDialogClose}
        onClose={this.onCreateDialogClose}
        title={this.props.intl.messages['projects.text.createSuccess']}
        className="success-dialog"
        language={'en-us'}
        footerAlign={this.state.footerAlign}>
{this.props.intl.messages['projects.text.createSuccess']}
</Dialog>
      </span>
        );
    }


}

export default injectIntl(CreateApplicationEnvironmentDialog)
