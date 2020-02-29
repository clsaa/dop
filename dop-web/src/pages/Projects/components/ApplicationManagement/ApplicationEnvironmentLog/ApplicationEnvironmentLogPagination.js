import {Loading, Pagination} from "@icedesign/base";
import React, {Component} from 'react';
import API from "../../../../API.js"
import Axios from "axios";
import ApplicationEnvironmentLogList from "./ApplicationEnvironmentLogList";
import "./ApplicationEnvironmentLogPagination.scss"
import {injectIntl} from "react-intl";


/**
 * 部署历史翻页器
 * @author Bowen
 **/
class ApplicationEnvironmentLogPagination extends Component {
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
            appEnvId: props.appEnvId,

            loading: true
        };
        this.handleChange = this.handleChange.bind(this);
    }


    //刷新列表数据
    refreshList(currentPage, searchKey) {
        this.setState({
            loading: true
        })
        let url = API.application + '/app/env/' + this.state.appEnvId + "/log"
        let _this = this;
        Axios.get(url, {
            params: {
                pageNo: currentPage,
                pageSize: 15,
            }
        })
            .then(function (response) {
                console.log(response)
                _this.setState({
                    current: currentPage,
                    pageSize: response.data.pageSize,
                    totalCount: response.data.totalCount,
                    currentData: response.data.pageList,
                    loading: false
                });
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


    render() {
        /*
        * 将应用列表作为翻页器的子组件，数据由翻页器传递给应用列表显示
         */
        return (
            <div>
                <Loading visible={this.state.loading} shape="dot-circle" color="#2077FF">
                    <ApplicationEnvironmentLogList
                        appEnvId={this.state.appEnvId}
                        currentData={this.state.currentData}
                    />
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
                    onChange={this.handleChange}
                    pageSize={this.state.pageSize}
                    total={this.state.totalCount}/>
            </div>
        )
    }

}


export default injectIntl(ApplicationEnvironmentLogPagination)


