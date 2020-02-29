import React,{Component} from 'react';
import {injectIntl,FormattedMessage} from 'react-intl';
import {Button,Feedback,Dialog} from '@icedesign/base'
import API from "../../../API";
import Axios from "axios";

const Toast = Feedback.toast;
class DeleteMemberDialog extends Component{
    constructor(props){
        super(props);
        this.state={
            footerAlign: "center",
            deleteDialogStyle: {
                width: "20%"
            },
            deleteDialogVisible: false,
            deleteKeys:[],
            refreshMemberList: this.props.refreshList,
            projectId:this.props.projectId
        }
    }
    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({
            deleteKeys: nextProps.deleteKeys,
            refreshMemberList:nextProps.refreshList,
            projectId:nextProps.projectId
        });
    }
    onDeleteMember= () =>{
        //移除对应的成员
        let url ="";
        let _this = this;
        for (let i = 0; i < this.state.deleteKeys.length; i++) {
            url = API.image + '/v1/projects/'+this.state.projectId+'/members/'+this.state.deleteKeys[i];
            Axios.delete(url, {})
                .then(function (response) {
                    Toast.success(_this.props.intl.messages["image.namespace.deleteSuccess"]);
                    _this.state.refreshMemberList();
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
            console.log(this.state.deleteKeys);
            Toast.error(this.props.intl.messages["image.member.deleteMessage"]);
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

        return(
            <span>
                <Button className="button" onClick={this.onOpen} type="primary" shape={"warning"}>
                    <FormattedMessage id="image.member.deleteMember"
                                      defaultMessage="移除成员"/>
                </Button>

                <Dialog language={this.props.intl.locale==='zh-CN'?'zh-cn':'en-us'}
                        visible={this.state.deleteDialogVisible}
                        onOk={this.onDeleteMember}
                        onCancel={this.onDeleteLogClose}
                        onClose={this.onDeleteLogClose}
                        style={this.state.deleteDialogStyle}
                        footerAlign={this.state.footerAlign}>
                <FormattedMessage id="image.member.deleteDecision"
                                  defaultMessage="确定要移除已经选择的成员？"/>
                </Dialog>
           </span>
        )
    }
}
export default injectIntl(DeleteMemberDialog)

