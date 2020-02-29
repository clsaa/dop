import {Dialog,Button,} from "@icedesign/base";
import NamespaceForm from "../CreateNamespaceForm/index"
import React, {Component} from 'react'
import "../../Style.scss"
import {injectIntl,FormattedMessage} from 'react-intl'

class CreateNamespaceDialog extends Component {
    constructor(props) {
        super(props);

        this.state = {
            //是否已经提交
            isSubmit: false,
            footerAlign: "center",
            visible: false,
            style: {
                width: "35%"
            },
            createDialogStyle: {
                width: "10%"
            },
            createDialogVisible: false,
            refreshProjectList: props.refreshProjectList
        }
    };

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

        this.state.refreshProjectList();
        console.log("finished");
    }

    render() {
        return (
            <span className={"dialog"}>
                <Button onClick={this.onOpen} type="primary">
                    <FormattedMessage id="image.createNamespace"
                            defaultMessage="创建命名空间"/>
                </Button>

                <Dialog
                    language={this.props.intl.locale==='zh-CN'?'zh-cn':'en-us'}
                    visible={this.state.visible}
                    onOk={this.onOk}
                    onCancel={this.onClose}
                    onClose={this.onClose}
                    title={this.props.intl.messages["image.createNamespace"]}
                    style={this.state.style}
                    footerAlign={this.state.footerAlign}
                >
                    <NamespaceForm
                        isSubmit={this.state.isSubmit}
                        finished={this.finished.bind(this)}/>
                </Dialog>

                <Dialog
                    language={this.props.intl.locale==='zh-CN'?'zh-cn':'en-us'}
                    visible={this.state.createDialogVisible}
                    onOk={this.onCreateDialogClose}
                    onCancel={this.onCreateDialogClose}
                    onClose={this.onCreateDialogClose}
                    title={this.props.intl.messages["image.createSuccess"]}
                    style={this.state.createDialogStyle}
                    footerAlign={this.state.footerAlign}>
                    <FormattedMessage id="image.namespace.successMessage"
                                defaultMessage="命名空间创建成功！"/>
                </Dialog>
            </span>
        );
    }


}
export default injectIntl(CreateNamespaceDialog)