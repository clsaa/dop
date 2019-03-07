/**
 *  功能点管理
 *  @author lizhiying
 *
 * */
import React, { Component } from 'react';

import Axios from 'axios';
import API from '../API';
import {Table, Button, Dialog, Radio, Field, Feedback} from '@icedesign/base';
import Pagination from "@icedesign/base/lib/pagination";
import Form from "@icedesign/base/lib/form";
import BalloonConfirm from "@icedesign/balloon-confirm";
import Input from "@icedesign/base/lib/input";
import Select, {Option} from "@icedesign/base/lib/select";


const { Item: FormItem } = Form;
const { Group: RadioGroup } = Radio;

export default class Permission extends Component {


    constructor(props) {
        super(props);
        this.field = new Field(this);
        this.state = {
            Dialogstyle: {
                width: "50%" ,height:"7000"
            },
            currentData : [],
            visible:false,
            deletevisible:false,
            InputInfo:
                {
                    parentId:"",
                    name:"",
                    muser:"",
                    cuser:"",
                    isPravte:"",
                    description:"",
                    isdeleted:false
                },
            pageNo:1,
            pageSize:8,
            totalCount:0
        };

    }
    handleReset(e) {
        e.preventDefault();
        this.field.reset();
    }

    // 验证名称是否重复
    userExists(rule, value, callback) {
        if (!value) {
            callback();
        } else {
            setTimeout(() => {

                let url = API.permission + "/v1/permissions/alldata" ;

                Axios.get(url).then((response) => {
                    // handle success
                    // 去get已经存储的数据
                    response.data.forEach((val)=>
                    {
                        //对response.data进行遍历，取每个数组的name项进行比较
                        console.log(val.name)
                     if(value==val.name)
                        {
                            callback([new Error("抱歉，该功能点名称已被使用。")]);
                        }
                    })
                    callback();
                }).catch((error)=>{
                    // handle error
                    console.log(error);
                });
            }, 1000);
        }
    }

    //取消删除操作
    onCancel = () => {
        console.log('取消删除');
        Feedback.toast.error('删除已取消！')
    }

    //弹出创建功能点弹窗
    onOpen = () => {
        this.setState({
            visible: true
        });
    };
    //关闭功能点弹窗
    onClose = reason => {
        console.log(reason);

        this.setState({
            visible: false
        });
    };

    onChange=currentPage=> {

        let url = API.permission + "/v1/permissions/pagealldata" ;
        let params=
            {
                pageNo:currentPage,
                pageSize:this.state.pageSize
            }
        // console.log(params)
        Axios.get(url,{params:(params)}).then((response) => {
            // handle success
            console.log(response);
            this.setState({currentData:response.data.pageList,
                pageNo:response.data.pageNo,
                totalCount:response.data.totalCount}
               );

        }).catch((error)=>{
            // handle error
            console.log(error);
        });
        console.log(this.state.pageNo)
    }

    onKeyDown(e, opts) {
        console.log('onKeyDown', opts);
    }


    //每次访问的刷新
    componentDidMount() {
        let url = API.permission + "/v1/permissions/pagealldata" ;
        let params=
            {
                pageNo:this.state.pageNo,
                pageSize:this.state.pageSize
            }
            // console.log(params)
        Axios.get(url,{params:(params)}).then((response) => {
            // handle success
            console.log(response.data);
            this.setState({currentData:response.data.pageList,
                pageNo:response.data.pageNo,
                totalCount:response.data.totalCount});

        }).catch((error)=>{
            // handle error
            console.log(error);
        }).then(()=>{
            // always executed

        });
    }

    //创建功能点
    handleSubmit(e) {
        this.setState({
            visible: false
        });
        e.preventDefault();
        this.field.validate((errors, values) => {
            if (errors) {
                console.log("Errors in form!!!");
                return;
            }
            let url = API.permission + "/v1/permissions" ;
            let params=
                {
                    cuser: values.cuser,
                    deleted: false,
                    description: values.description,
                    isPrivate: values.isPrivate,
                    muser: values.cuser,
                    name:values.name,
                    parentId: 0,
                }

            console.log("Submit!!!");
            console.log(values);

            Axios.post(url, {},{params:(params)}
            )
                .then((response)=>{
                console.log(response);
            }).catch((error)=> {
                console.log(error);

            });
        });
    }

    //确认删除操作以及
    //删除功能点
    onConfirm = id => {

        const { dataSource } = this.state;
        let url = API.permission + "/v1/permissions" ;
        let params= {id:id}
        Axios.delete(url,{params:(params)}
        )
            .then((response)=>{
                Feedback.toast.success('成功删除！')
                console.log(response);
            }).catch((error)=> {
            console.log(error);
        });
    };

    render() {
        const { init, getError, getState } = this.field;
        //form样式定义
        const formItemLayout = {
            labelCol: {
                span: 6
            },
            wrapperCol: {
                span: 14
            }
        };
        //窗口按钮定义
        const footer = (
            <a onClick={this.onClose} href="javascript:;">
                取消
            </a>
        );
        //删除操作定义
        const renderdelete = (value, index, record) => {
            return (
                <BalloonConfirm
                    onConfirm={this.onConfirm.bind(this, record.id)}
                    onCancel={this.onCancel}
                    title="您真的要删除吗？"
                >
                    <button >删除</button>
                </BalloonConfirm>

            );
        }
        return (
        <div>
            <Button onClick={this.onOpen} >创建功能点</Button>
            <Dialog
                title="创建功能点"
                visible={this.state.visible}
                onClose={this.onClose}
                style={this.state.Dialogstyle}
                minMargin={5}
                footer={footer}>

                <Form field={this.field}>

                    <FormItem
                        label="功能点名称："
                        {...formItemLayout}
                        hasFeedback
                        help={
                            getState("name") === "validating"
                                ? "校验中..."
                                : (getError("name") || []).join(", ")
                        }
                    >
                        <Input
                            maxLength={10}
                            hasLimitHint
                            placeholder="请输入名称"
                            {...init("name", {
                                rules: [
                                    { required: true, min: 1, message: "名称不能为空！" },
                                    //检验名称是否重复
                                    { validator: this.userExists }
                                ]
                            })}
                        />
                    </FormItem>
                    <FormItem
                        label="创建者："
                        {...formItemLayout}

                    >
                        <Input
                            maxLength={10}
                            hasLimitHint
                            {...init("cuser", {
                                rules: [
                                    { required: true, min: 1, message: "必须注明创建者！" },

                                ]
                            })}
                        />
                    </FormItem>
                    <FormItem label="是否私有：" hasFeedback {...formItemLayout}>
                        <RadioGroup
                            {...init("isPrivate", {
                                rules: [{ required: true, message: "请定义是否私有" }]
                            })}
                        >
                            <Radio value="0">公有</Radio>
                            <Radio value="1">私有</Radio>
                        </RadioGroup>
                    </FormItem>
                    <FormItem label="功能点描述：" {...formItemLayout}>
                        <Input
                            multiple
                            maxLength={30}
                            hasLimitHint
                            placeholder="请描述该功能点具体内容"
                            {...init("description", {
                                rules: [{ required: true, message: "真的不打算写点什么吗？" }]
                            })}
                        />
                    </FormItem>
                    <FormItem label="父功能点ID：" {...formItemLayout} required>
                        <Select style={{ width: 200 }} {...init("parentId")}>
                            <Option value="1">1</Option>
                            <Option value="2">2</Option>

                            <Option value="3">3</Option>
                            <Option value="disabled" disabled>
                            disabled
                            </Option>
                        </Select>
                    </FormItem>
                    <FormItem wrapperCol={{ offset: 6 }}>
                        <Button type="primary" onClick={this.handleSubmit.bind(this)}>
                            确定
                        </Button>
                        &nbsp;&nbsp;&nbsp;
                        <Button onClick={this.handleReset.bind(this)}>重置</Button>
                    </FormItem>

                </Form>
            </Dialog>



            {/*<Button onClick={}>查询所有功能点</Button>*/}


            <Table dataSource={this.state.currentData}>
                <Table.Column title="功能点名称" dataIndex="name"/>
                <Table.Column title="功能点描述" dataIndex="description"/>
                <Table.Column title="创建人" dataIndex="cuser"/>
                <Table.Column title="创建时间" dataIndex="ctime"/>
                <Table.Column title="删除操作" cell={renderdelete} width="20%" />

            </Table>
            <Pagination total={this.state.totalCount}
                        current={this.state.pageNo}
                        onChange={this.onChange}
                        pageSize={this.state.pageSize
                }
            className="page-demo" />
        </div>




        );
    }
}