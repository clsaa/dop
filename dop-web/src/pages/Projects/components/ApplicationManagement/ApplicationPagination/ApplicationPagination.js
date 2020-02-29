import {Loading, Pagination} from "@icedesign/base";
import React, {Component} from 'react';
import API from "../../../../API.js"
import Axios from "axios";
import ApplicationList from "../ApplicationList/ApplicationList";
import "./ApplicationPagination.scss"
import {injectIntl} from "react-intl";


/**
 * 应用列表翻页器
 * @author Bowen
 **/
class ApplicationPagination extends Component {
    constructor(props) {
        super(props);

        this.state = {
            //当前页数
            current: 1,
            //当前页的数据，传递给子组件渲染
            currentData: [],
            //总页数
            totalPage: 1,
            //当前页面显示的应用所属的项目id
            projectId: props.projectId,
            // 搜索的Key
            searchKey: props.searchKey,
            loading: true
        };
        this.handleChange = this.handleChange.bind(this);
    }


    //刷新列表数据
    refreshList(currentPage, searchKey) {
        this.setState({
            loading: true
        })
        let url = API.application + '/pagedapp'
        let _this = this;
        Axios.get(url, {
            params: {
                projectId: this.state.projectId,
                pageNo: currentPage,
                pageSize: 15,
                queryKey: searchKey
            }
        })
            .then(function (response) {
                console.log(response)
                _this.setState({
                    current: currentPage,
                    pageSize: response.data.pageSize,
                    searchKey: searchKey,
                    totalCount: response.data.totalCount,
                    currentData: response.data.pageList,
                    loading: false
                });
                // tmpData = response.data.pageList

                //     //获取用户数据
                //
                //
                //     //使用SET数组去重，获取所有不重复的用户ID
                //     let userIdList = new Set();
                //     for (let i = 0; i < tmpData.length; i++) {
                //         userIdList.add(tmpData[i].ouser);
                //     }
                //
                //
                //     //存放所有请求URL的数组
                //     let getList = []
                //     for (let id of userIdList) {
                //         let nameUrl = API.gateway + '/user-server/v1/users/' + id;
                //         getList.push(Axios.get(nameUrl));
                //     }
                //
                //     //存放最终结果的数组，使用finalList[ID]---NAME的哈希映射
                //     let finalList = {};
                //
                //     //将所有URL请求发出
                //     Axios.all(getList).then(Axios.spread(function (...resList) {
                //
                //
                //         for (let i = 0; i < resList.length; i++) {
                //             //如果该值不为空则添加到哈希表中
                //             if (resList[i].data !== "") {
                //                 finalList[resList[i].data.id.toString()] = resList[i].data.name;
                //             }
                //         }
                //
                //         //将所有ID置换为NAME
                //         for (let i = 0; i < tmpData.length; i++) {
                //             tmpData[i].ouser = finalList[tmpData[i].ouser.toString()];
                //         }
                //
                //
                //         //赋值
                //         _this.setState({
                //             currentData: tmpData,
                //             loading: false
                //         });
                //     }))
            })
            .catch(function (error) {
                _this.setState({
                    loading: false
                })
            });
    }


    /*
     *
     * 分页器改变页码时
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
        this.refreshList(1, this.state.searchKey);
    }

    //删除应用后请求刷新
    deletedCallRefresh() {
        this.refreshList(1, this.state.searchKey);
    }

    render() {
        /*
        * 将应用列表作为翻页器的子组件，数据由翻页器传递给应用列表显示
         */
        return (
            <div>
                <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF">
                    <ApplicationList
                        projectId={this.state.projectId}
                        currentData={this.state.currentData}
                                     deletedCallRefresh={this.deletedCallRefresh.bind(this)}/>
                </Loading>

                <Pagination
                    locale={{
                        prev: this.props.intl.messages["projects.text.prev"],
                        next: this.props.intl.messages["projects.text.next"],
                        goTo: this.props.intl.messages["projects.text.goto"],
                        page: this.props.intl.messages["projects.text.page"],
                        go: this.props.intl.messages["projects.text.go"],
                    }}
                    projectId={this.state.projectId}
                    className="pagination"
                    current={this.state.current}
                    onChange={this.handleChange}
                    pageSize={this.state.pageSize}
                    total={this.state.totalCount}
                />
            </div>
        )
    }

}


export default injectIntl(ApplicationPagination)



