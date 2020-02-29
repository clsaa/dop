import React, {Component} from 'react';
import {Grid, Table} from '@icedesign/base';
import {Col} from "@alifd/next/lib/grid";
import {Link} from 'react-router-dom';
import "./ProjectList.scss"
import {injectIntl} from "react-intl";

const {Row} = Grid;


/**
 * 展示项目列表
 * @author Bowen
 **/

class ProjectList extends Component {

    static displayName = 'ProjectList';


    constructor(props) {
        super(props);

        console.log(props.currentData)
        //接受来自分页器的参数即当前页数据
        this.state = {
            currentData: props.currentData
        };
        console.log("constructor");

    }


    componentWillReceiveProps(nextProps, nextContext) {
        // let url = API.application + '/projects';
        // for (let i = 0; i < nextProps.currentData.length; i++) {
        //     let tmpTime = nextProps.currentData[i].ctime;
        //     if (tmpTime[0].length < 4) break;
        //     nextProps.currentData[i].ctime = tmpTime[0] + "/" + tmpTime[1] + "/" + tmpTime[2] + " " + tmpTime[3] + ":" + tmpTime[4];
        // }
        this.setState({
            currentData: nextProps.currentData
        });
    }

    //链接 点击后跳转到对应projectId所包含的所有应用列表的页面
    nameRender = function (value, index, record) {
        return <Link to={"/projectDetail?projectId=" + record.id}
        >{value}</Link>
    }
    render() {
        return (
            <Row wrap gutter="20">
                <Col>
                    <Table dataSource={this.state.currentData}
                           locale={{empty: this.props.intl.messages["projects.text.noData"]}}>
                        <Table.Column
                            title={this.props.intl.messages["projects.text.projectId"]}
                            dataIndex="id"/>

                        <Table.Column title={this.props.intl.messages["projects.text.projectName2"]}
                                      cell={this.nameRender.bind(this)}
                                      dataIndex="title"/>

                        <Table.Column title={this.props.intl.messages["projects.text.creator"]}
                                      dataIndex="cuserName"/>

                        <Table.Column title={this.props.intl.messages["projects.text.createTime"]}
                                      dataIndex="ctime"/>
                    </Table>
                </Col>
            </Row>
        );
    }

}

export default injectIntl(ProjectList)