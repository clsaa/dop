import React from 'react';
import {Table} from '@icedesign/base';
import {Link, withRouter} from 'react-router-dom';
import API from "../../API";
import Axios from 'axios';
import { Loading } from "@icedesign/base";
import Spinner from '../components/Spinner';

import './ProjectList.css';

import imgPublic from './imgs/public.png'
import imgStar from './imgs/star.png'
import imgPrivate from './imgs/private.png'

import {injectIntl,FormattedMessage} from 'react-intl';


const spinner=(
    <Spinner/>
);

class ProjectList extends React.Component {


    constructor(props) {
        super(props);
        const {sort}=this.props.match.params;
        this.state = {
            sort: sort,
            data: [],
            showData:[],
            pageNo: 1,
            pageSize: 5,
            pageTotal: 0,
            loadingVisible:true,
        };
    }

    selectClassPersonal() {

        if (this.state.sort === 'personal') {
            return "menu-item-active";
        } else {
            return "menu-item";
        }
    }

    selectClassStarred() {

        if (this.state.sort === 'starred') {
            return "menu-item-active";
        } else {
            return "menu-item";
        }
    }

    selectClassAll() {

        if (this.state.sort === 'all') {
            return "menu-item-active";
        } else {
            return "menu-item";
        }
    }


    componentWillMount() {

        let url = API.code + "/projects?sort=" + this.state.sort + "&userId=" + sessionStorage.getItem("user-id");
        let self = this;
        Axios.get(url).then((response) => {
            const pageTotal = Math.ceil(response.data.length / this.state.pageSize);
            self.setState({pageTotal: pageTotal, data: response.data,showData:response.data,loadingVisible:false});
        })

    }

    changeSort(sort) {
        this.props.history.push("/code/projects/"+sort);
    }

    prePage() {
        this.setState({
            pageNo: this.state.pageNo > 1 ? this.state.pageNo - 1 : 1
        })
    }

    nextPage() {
        this.setState({
            pageNo: this.state.pageNo < this.state.pageTotal ? this.state.pageNo + 1 : this.state.pageTotal
        })
    }

    handleInputChange(event) {
        const val = event.target.value;
        if (val !== "") {
            const showData = this.state.data.filter(function (item) {
                const name = item.path_with_namespace.substring(item.path_with_namespace.indexOf('/')+1);
                return name.indexOf(val) !== -1;
            });
            const pageTotal=Math.ceil(showData.length/this.state.pageSize);
            this.setState({
                pageTotal:pageTotal,
                pageNo:1,
                showData:showData
            })
        }else {
            const pageTotal=Math.ceil(this.state.data.length/this.state.pageSize);
            this.setState({
                pageTotal:pageTotal,
                pageNo:1,
                showData:this.state.data
            })
        }

    }

    newProjectLink(){
        this.props.history.push("/code/projects/new");
    }


    render() {


        return (
            <div className="container">
                <div>
                    <ul className="center-top-menu">
                        <li>
                            <a className={this.selectClassPersonal()} onClick={this.changeSort.bind(this, "personal")}>
                                <FormattedMessage id="code.projectlist.personal"/>
                            </a>
                        </li>
                        <li>
                            <a className={this.selectClassStarred()} onClick={this.changeSort.bind(this, "starred")}>
                                <FormattedMessage id="code.projectlist.starred"/>
                            </a>
                        </li>
                        <li>
                            <a className={this.selectClassAll()} onClick={this.changeSort.bind(this, "all")}>
                                <FormattedMessage id="code.projectlist.all"/>
                            </a>
                        </li>
                    </ul>
                    <div className="search-form">
                        <input className="input_search" placeholder={this.props.intl.messages["code.projectlist.placeholder"]} onChange={this.handleInputChange.bind(this)}/>
                        <button onClick={this.newProjectLink.bind(this)} className="btn_add_project">
                            <span className="text_new">+<FormattedMessage id="code.projectlist.new"/></span>
                        </button>
                    </div>
                    <Loading visible={this.state.loadingVisible} className="loading-project-list" tip={spinner}>
                        <div>
                            <ul>
                                {(() => {
                                    let begin = (this.state.pageNo - 1) * this.state.pageSize;
                                    let end = Math.min(this.state.pageNo * this.state.pageSize, this.state.showData.length);
                                    return this.state.showData.slice(begin, end).map((item) => {
                                        const path = "/code/" + item.path_with_namespace;
                                        return (
                                            <li className="list-item">
                                                <div className="list-item-avatar">
                                                    {item.name.substring(0, 1).toUpperCase()}
                                                </div>
                                                <div className="list-item-intro">
                                                    <div><Link to={path}><span
                                                        className="text_path">{item.name_with_namespace}</span></Link></div>
                                                    <div><span className="text_description">{item.description}</span></div>
                                                </div>
                                                <div className="list-item-tag">
                                                    <img src={imgStar}/>
                                                    <span>{item.star_count}</span>
                                                    {
                                                        (() => {
                                                            if (item.visibility === "public") {
                                                                return <img src={imgPublic}/>
                                                            } else {
                                                                return <img src={imgPrivate}/>
                                                            }
                                                        })()
                                                    }

                                                </div>
                                            </li>
                                        );
                                    })
                                })()
                                }
                            </ul>

                        </div>
                    </Loading>
                </div>

                <div className="div_page">
                    <button className="btn_page" onClick={this.nextPage.bind(this)}><FormattedMessage id="code.projectlist.nextpage"/></button>
                    <button className="btn_page">{this.state.pageNo}<FormattedMessage id="code.projectlist.page"/>/{this.state.pageTotal}<FormattedMessage id="code.projectlist.page"/></button>
                    <button className="btn_page" onClick={this.prePage.bind(this)}><FormattedMessage id="code.projectlist.prepage"/></button>
                </div>

            </div>
        );


    }

}

export default injectIntl((props)=><ProjectList {...props} key={props.location.pathname} />)






