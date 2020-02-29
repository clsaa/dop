import React, {Component} from 'react';
import {injectIntl} from 'react-intl';
import 'antd/dist/antd.css';
import { Table } from 'antd';

const columns = [
    {
        title: 'Name',
        dataIndex: 'name',
        width: 150,
    },
    {
        title: 'Age',
        dataIndex: 'age',
        width: 150,
    },
    {
        title: 'Address',
        dataIndex: 'address',
    },
];
const data = [];
for (let i = 0; i < 100; i++) {
    data.push({
        key: i,
        name: `Edward King ${i}`,
        age: 32,
        address: `London, Park Lane no. ${i}`,
    });
}
class PipelineTest extends Component {

    render() {
        return (
            <div>
                <Table columns={columns} dataSource={data} pagination={{ pageSize: 50 }} />,
            </div>
        );
    }
}



export default injectIntl(PipelineTest)
