import {Loading, Pagination} from "@icedesign/base";
import React, {Component} from 'react';
import API from "../../../../API.js"
import Axios from "axios";
import ProjectList from "../ProjectList/ProjectList";
import "./ProjectPagination.scss"
import {injectIntl} from "react-intl";


/**
 * 项目列表翻页器
 * @author Bowen
 **/
class ProjectPagination extends Component {
    constructor(props) {
        super(props);

        this.state = {
            //当前页数
            current: 1,
            //当前页的数据
            currentData: [],
            //总页数
            totalPage: 1,
            queryKey: props.searchKey,
            loading: true
            // searchData: [],
            // searchKey: ""
        };

        this.handleChange = this.handleChange.bind(this);
    }

    //刷新数据列表

    refreshList(current, key) {
        this.setState({
            loading: true
        })


        let url = API.application + '/paged-project';
        let _this = this;
        Axios.get(url, {
            params: {
                pageNo: current,
                pageSize: 15,
                includeFinished: false,
                queryKey: key
            }
        })
            .then(function (response) {
                console.log(response)
                _this.setState({
                    current: current,
                    pageSize: response.data.pageSize,
                    totalCount: response.data.totalCount,
                    currentData: response.data.pageList,
                    loading: false
                });

                // //获取用户数据
                // tmpData = response.data.pageList;
                //
                // //使用SET数组去重，获取所有不重复的用户ID
                // let userIdList = new Set();
                // console.log(tmpData)
                // for (let i = 0; i < tmpData.length; i++) {
                //     userIdList.add(tmpData[i].cuser);
                // }
                // console.log("userIdList", userIdList)
                //
                // //存放所有请求URL的数组
                // let getList = []
                // for (let id of userIdList) {
                //     let nameUrl = API.gateway + '/user-server/v1/users/' + id;
                //     getList.push(Axios.get(nameUrl));
                // }
                // console.log("getList", getList)
                //
                // //存放最终结果的数组，使用finalList[ID]---NAME的哈希映射
                // let finalList = {};
                //
                // //将所有URL请求发出
                // Axios.all(getList).then(Axios.spread(function (...resList) {
                //     console.log("resList", resList);
                //     for (let i = 0; i < resList.length; i++) {
                //         //如果该值不为空则添加到哈希表中
                //         if (resList[i].data !== "") {
                //             finalList[resList[i].data.id.toString()] = resList[i].data.name;
                //         }
                //     }
                //     console.log("finalList", finalList)
                //
                //     //将所有ID置换为NAME
                //     for (let i = 0; i < tmpData.length; i++) {
                //         tmpData[i].cuser = finalList[tmpData[i].cuser.toString()];
                //     }
                //     console.log(tmpData);
                //     //赋值
                //     _this.setState({
                //         currentData: tmpData,
                //         loading: false
                //     });
                // }))

            })
            .catch(function (error) {
                _this.setState({
                    loading: false
                });
            });

    }

    /*
     *
     * 当处理改变（分页器改变、父组件请求刷新）
     */
    handleChange(current) {
        this.refreshList(current, this.state.searchKey);
    }

    /**
     *
     * 接受来自父组件的刷新请求
     *
     */
    componentWillReceiveProps(nextProps, nextContext) {
        let key = nextProps.searchKey;
        this.refreshList(1, key);
    }


    /*
    *加载初始数据
     */
    componentDidMount() {
        this.refreshList(1, "");
    }

    render() {
        return (
            /*
        * 将项目列表作为翻页器的子组件，数据由翻页器传递给应用列表显示
         */
            <div>
                <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF">
                    <ProjectList currentData={this.state.currentData}/>
                </Loading>
                <Pagination
                    locale={{
                        prev: this.props.intl.messages["projects.text.prev"],
                        next: this.props.intl.messages["projects.text.next"],
                        goTo: this.props.intl.messages["projects.text.goto"],
                        page: this.props.intl.messages["projects.text.page"],
                        go: this.props.intl.messages["projects.text.go"],
                    }}
                    className="pagination"
                            current={this.state.current}
                            onChange={this.handleChange.bind(this)}
                            pageSize={this.state.pageSize}
                            total={this.state.totalCount}/>
            </div>
        )
    }

}

export default injectIntl(ProjectPagination)





