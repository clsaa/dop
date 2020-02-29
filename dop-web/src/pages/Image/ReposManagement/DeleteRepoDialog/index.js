import React,{Component} from 'react';
import {Dialog,Feedback,Button} from '@icedesign/base';
import Axios from "axios";
import API from "../../../API";
import {injectIntl,FormattedMessage} from 'react-intl';


const Toast = Feedback.toast;
 class DeleteRepoDialog extends Component{


    constructor(props){
        super(props);
        this.state={
            footerAlign: "center",
            style: {
                width: "30%"
            },
            deleteDialogStyle: {
                width: "15%"
            },
            deleteDialogVisible: false,
            deleteKeys:[],
            refreshRepoList: this.props.refreshRepoList
        }
    }
    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({
            deleteKeys: nextProps.deleteKeys
        });
    }
    onDeleteRepo = () =>{

        //删除对应的镜像仓库
        let url ="";
        let _this = this;
        for (let i = 0; i < this.state.deleteKeys.length; i++) {
            url = API.image + "/v1/repositories/"+this.state.deleteKeys[i];
            Axios.delete(url, {})
                .then(function (response) {
                    Toast.success(_this.props.intl.messages["image.namespace.deleteSuccess"]);
                    _this.state.refreshRepoList();
                    console.log(response.status);
                }).catch(function (error) {
                console.log(error);
                Toast.error(_this.props.intl.messages["image.namespace.deleteFailed"]);
            });
        }
        this.setState({
            deleteDialogVisible: false
        })

    }


    onOpen = () => {
        if(this.state.deleteKeys.length===0){
            Toast.error(this.props.intl.messages["image.repository.deleteMessage"]);
        }else {
            this.setState({
                deleteDialogVisible: true
            });
        }
    };


    onDeleteLogClose =()=>{
        this.setState({
            deleteDialogVisible:false
        })
    };

    render() {
        return (
            <span>
                <Button onClick={this.onOpen} type="primary" shape={"warning"}>
                    <FormattedMessage id="image.repository.delete"
                                      defaultMessage="删除镜像仓库"/></Button>

                <Dialog language={this.props.intl.locale==='zh-CN'?'zh-cn':'en-us'}
                        visible={this.state.deleteDialogVisible}
                        onOk={this.onDeleteRepo}
                        onCancel={this.onDeleteLogClose}
                        onClose={this.onDeleteLogClose}
                        style={this.state.deleteDialogStyle}
                        footerAlign={this.state.footerAlign}>
                    <FormattedMessage id="image.repository.deleteDecision"
                                      defaultMessage="确定要删除已选择的镜像仓库吗？"/>
                </Dialog>
            </span>
        );
    }

}
export default injectIntl(DeleteRepoDialog)