import React, {Component} from 'react';
import {Balloon, Grid, Icon, Table} from '@icedesign/base';
import {Col} from "@alifd/next/lib/grid";
import {Link} from "react-router-dom";
import "./ApplicationEnvironmentLogList.scss"
import {injectIntl} from "react-intl";
import PipelineLogDialog from "./PipelineLogDialog";

const {Row} = Grid;
/**
 * 展示部署历史的页面
 * @author Bowen
 **/

class ApplicationEnvironmentLogList extends Component {

    static displayName = 'ApplicationEnvironmentLogList';


    constructor(props) {
        super(props);

        //接受来自分页器的参数即当前页数据
        this.state = {
            appEnvId: props.appEnvId,
            currentData: props.currentData
        };

    }

    //接受来自分页器的参数即当前页数据
    componentWillReceiveProps(nextProps, nextContext) {
        this.setState({
            currentData: nextProps.currentData
        });
    }

    //渲染表格中的ID项，添加超链接至该项目ID下的应用ID列表
    codeIdRender(url) {
        let _this = this;
        // https://github.com/zhangfuli/simple-maven-pipeline/dee981ce6640e3a0b115c8e784b78357f8f1ad15
        // github.com/clsaa/dop/commit/011fd4191f7c9b3e92539eb63152b8670b003253
        // console.log(url.split("/"))
        let infoList = url.split("/")
        let userName = infoList[3]
        let projectName = infoList[4]
        let commitId = infoList[infoList.length - 1]
        ///code/:username/:projectname/commit/:sha
        return <Link to={"/code/" + userName + "/" + projectName + "/commit/" + commitId}
        >{commitId}</Link>
    }

    linkTest() {
        this.props.history.push()
    }

    //渲染表格中的ID项，添加超链接至该项目ID下的应用ID列表
    imageIdRender(imageUrl) {
        let _this = this;
        // registry.dop.clsaa.com/dop/dop-web:201904131311d357d70
        let buildTag = imageUrl.split(":")[1]
        let projectName = imageUrl.split("/")[1]
        // console.log("repoName", imageUrl.split("/"))
        let repoName = imageUrl.split("/")[2].split(":")[0]
        return <Link to={"/repos/" + projectName + "/" + repoName + "/images/" + buildTag}
        >{buildTag}</Link>
    }


    render() {
        const defaultTrigger = (

            <h3>{this.props.intl.messages['projects.text.viewEnvLog']}</h3>
        );
        const renderOpr = (value, index, record) => {
            // console.log("record:", record, value)

            return <div>{record.ctime}
                <Icon onClick={this.popupConfirm.bind(this, record.id)} type="ashbin" className="delete-icon"/>

            </div>
        };
        const runningIdRender = (runningId) => {
            return <PipelineLogDialog runningId={runningId}/>
        }
        const envRender = (envData) => {
            // console.log("envData", envData)
            return (<Balloon align="rt" trigger={defaultTrigger} closable={false}>
                <pre>{envData}</pre>
            </Balloon>)
        };
        // const Content = (envData) => (
        //     <div>
        //         <h4 style={{ marginTop: 0 }}>balloon title</h4>
        //         <hr />
        //         <p>{envData}</p>
        //     </div>
        // );

        return (
            <Row wrap gutter="20">
                <Col>
                    <Table dataSource={this.state.currentData}>
                        <Table.Column title={this.props.intl.messages['projects.text.runningId']}
                                      cell={runningIdRender.bind(this)}
                                      dataIndex="id"/>

                        <Table.Column
                            cell={this.codeIdRender.bind(this)}
                            title={this.props.intl.messages['projects.text.commitId']}
                            dataIndex="commitUrl"/>

                        <Table.Column cell={this.imageIdRender.bind(this)}
                                      title={this.props.intl.messages['projects.text.buildTag']}
                                      dataIndex="imageUrl"/>

                        <Table.Column
                            cell={envRender.bind(this)}
                            title={this.props.intl.messages['projects.text.envInfo']}
                            dataIndex="appEnvLog"/>

                        <Table.Column title={this.props.intl.messages['projects.text.deploymentTime']}
                                      dataIndex="rtime"/>

                        <Table.Column title={this.props.intl.messages['projects.text.deploymentMember']}
                                      dataIndex="ruserName"/>

                        <Table.Column title={this.props.intl.messages['projects.text.status']}
                                      dataIndex="status"/>
                    </Table>
                </Col>
            </Row>
        );
    }

}

export default injectIntl(ApplicationEnvironmentLogList)
