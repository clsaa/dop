import React,{Component} from 'react';
import {Dialog,Feedback,Button} from '@icedesign/base';
import Axios from "axios";
import API from "../../../API";
import '../../Style.scss'
import {injectIntl,FormattedMessage} from 'react-intl';

const Toast = Feedback.toast;
class DeleteNameSpaceDialog extends Component{


    constructor(props){
        super(props);
        this.state={
            footerAlign: "center",
            deleteDialogStyle: {
                width: "20%"
            },
            deleteDialogVisible: false,
            deleteKeys:[],
            refreshProjectList: this.props.refreshProjectList,
        }
    }
    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({
            deleteKeys: nextProps.deleteKeys
        });
    }
    onDeleteNamespace = () =>{
        //删除对应的命名空间
        let url ="";
        let _this = this;
        for (let i = 0; i < this.state.deleteKeys.length; i++) {
            url = API.image + '/v1/projects/'+this.state.deleteKeys[i];
            Axios.delete(url, {})
                .then(function (response) {
                Toast.success(_this.props.intl.messages["image.namespace.deleteSuccess"]);
                _this.state.refreshProjectList();
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
            Toast.error(this.props.intl.messages["image.namespace.deleteMessage"]);
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
                <Button className="button" onClick={this.onOpen} type="primary" shape={"warning"}>
                    <FormattedMessage id="image.deleteNamespace"
                                      defaultMessage="删除命名空间"/>
                </Button>

                <Dialog language={this.props.intl.locale==='zh-CN'?'zh-cn':'en-us'}
                        visible={this.state.deleteDialogVisible}
                        onOk={this.onDeleteNamespace}
                        onCancel={this.onDeleteLogClose}
                        onClose={this.onDeleteLogClose}
                        style={this.state.deleteDialogStyle}
                        footerAlign={this.state.footerAlign}>
                <FormattedMessage id="image.namespace.deleteDecision"
                                  defaultMessage="确定要删除已选命名空间？"/>
                </Dialog>
           </span>
        );
    }

}
export default injectIntl(DeleteNameSpaceDialog);