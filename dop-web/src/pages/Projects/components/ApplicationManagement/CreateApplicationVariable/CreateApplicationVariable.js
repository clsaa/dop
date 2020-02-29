import {Button, Dialog} from "@icedesign/base";

import React, {Component} from 'react';
import "./CreateApplicationVariable.scss"
import {injectIntl} from "react-intl";
import CreateApplicationVariableForm from './CreateApplicationVariableForm'

/**
 *  创建应用变量的弹窗
 *  @author Bowen
 *
 * */



/**
 *    创建应用变量的弹窗
 *
 * */
class CreateApplicationVariableDialog extends Component {


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
            refreshApplicationVariableList: props.refreshApplicationVariableList,
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

        this.state.refreshApplicationVariableList();
        console.log("finished");
    }

    render() {
        return (
            <span>
                <Button onClick={this.onOpen}
                        type="primary"

                >
          {this.props.intl.messages['projects.button.createVar']}
        </Button>
        <Dialog
            visible={this.state.visible}
            onOk={this.onOk}
            onCancel={this.onClose}
            onClose={this.onClose}
            title={this.props.intl.messages['projects.button.createVar']}
            className="create-app-var-dialog"
            locale={{
                ok: this.props.intl.messages["projects.button.confirm"],
                cancel: this.props.intl.messages["projects.button.cancel"]
            }}
            footerAlign={this.state.footerAlign}
        >
          <CreateApplicationVariableForm isSubmit={this.state.isSubmit} finished={this.finished.bind(this)}
                                         appId={this.state.appId}/>
        </Dialog>

<Dialog visible={this.state.createDialogVisible}
        onOk={this.onCreateDialogClose}
        onCancel={this.onCreateDialogClose}
        onClose={this.onCreateDialogClose}
        title={this.props.intl.messages['projects.text.createSuccess']}
        className="success-dialog"
        footerAlign={this.state.footerAlign}>
{this.props.intl.messages['projects.text.createSuccess']}
</Dialog>
      </span>
        );
    }


}

export default injectIntl(CreateApplicationVariableDialog)