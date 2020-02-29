import React,{Component} from 'react';
import {Dialog,Feedback,Button} from '@icedesign/base';
import Axios from "axios";
import API from "../../../API";
import {injectIntl,FormattedMessage} from "react-intl";


const Toast = Feedback.toast;
class DeleteImageDialog extends Component{


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
            repoName:this.props.repoName,
            refreshImageList: this.props.refreshImageList
        }
    }
    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({
            deleteKeys: nextProps.deleteKeys,
            repoName:nextProps.repoName
        });

    }
    onDeleteRepo = () =>{
        console.log(this.state.repoName);
        //删除对应的镜像
        let url ="";
        let _this = this;
        for (let i = 0; i < this.state.deleteKeys.length; i++) {
            url = API.image + "/v1/repositories/"+this.state.repoName+"/images/"+this.state.deleteKeys[i];
            Axios.delete(url, {})
                .then(function (response) {
                    Toast.success(_this.props.intl.messages["image.namespace.deleteSuccess"]);
                    _this.state.refreshImageList(1,"");
                    console.log("删除镜像");
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
            Toast.error(this.props.intl.messages["image.imageInfo.deleteMessage"]);
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
                <Button onClick={this.onOpen} type="primary" shape="warning">
                    <FormattedMessage id="image.imageInfo.delete" defaultMessage="删除镜像"/>
                </Button>

                <Dialog language={this.props.intl.locale==='zh-CN'?'zh-cn':'en-us'}
                        visible={this.state.deleteDialogVisible}
                        onOk={this.onDeleteRepo}
                        onCancel={this.onDeleteLogClose}
                        onClose={this.onDeleteLogClose}
                        style={this.state.deleteDialogStyle}
                        footerAlign={this.state.footerAlign}>
                    <FormattedMessage id="image.imageInfo.deleteDecision" defaultMessage="确定要删除已选择的镜像？"/>
                </Dialog>
            </span>
        );
    }

}
export default injectIntl(DeleteImageDialog)