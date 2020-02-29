import React,{Component} from 'react';
import {Button,Dialog,Feedback} from "@icedesign/base";
import ProjectMemberForm from "../ProjectMemberForm"
import {injectIntl,FormattedMessage} from "react-intl";
const Toast = Feedback.toast;
 class AddMemberDialog extends Component{

    constructor(props) {
        super(props);

        this.state = {
            //是否已经提交
            isSubmit: false,
            footerAlign: "center",
            visible: false,
            style: {
                width: "32%"
            },
            createDialogStyle: {
                width: "15%"
            },
            createDialogVisible: false,
            refreshMemberList: this.props.refreshList,
            projectId : this.props.projectId
        }
    };

    onClose = () => {
        this.setState({
            visible: false
        });
    };

    onOk = () => {
        console.log("提交表单");
        this.setState({
            isSubmit: true
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

    finished(value) {
        if (value==="success"){
            this.setState({
                visible: false,
                createDialogVisible: true,
                isSubmit: false
            })

            this.state.refreshMemberList();
        }else {
            this.setState({
                isSubmit: false
            })
            Toast.error(this.props.intl.messages["image.member.addFailed"])
        }

    }


    render() {
        return (
            <span className={"dialog"}>
                <Button onClick={this.onOpen} type="primary">
                    <FormattedMessage id="image.addMember"
                                      defaultMessage="添加成员"/>
                </Button>
        <Dialog language={this.props.intl.locale==='zh-CN'?'zh-cn':'en-us'}
            visible={this.state.visible}
            onOk={this.onOk}
            onCancel={this.onClose}
            onClose={this.onClose}
            title={this.props.intl.messages["image.addMember.title"]}
            style={this.state.style}
            footerAlign={this.state.footerAlign}
        >
          <ProjectMemberForm
              isSubmit={this.state.isSubmit}
              finished={this.finished.bind(this)} projectId={this.state.projectId}/>
        </Dialog>

        <Dialog language={this.props.intl.locale==='zh-CN'?'zh-cn':'en-us'}
                visible={this.state.createDialogVisible}
                onOk={this.onCreateDialogClose}
                onCancel={this.onCreateDialogClose}
                onClose={this.onCreateDialogClose}
                title={this.props.intl.messages["image.addMember.addSuccess"]}
                style={this.state.createDialogStyle}
                footerAlign={this.state.footerAlign}>
            <FormattedMessage id="image.addMember.successMessage"
                              defaultMessage="成功添加成员到命名空间！"/>
        </Dialog>
      </span>
        );
    }
}

export default injectIntl(AddMemberDialog);