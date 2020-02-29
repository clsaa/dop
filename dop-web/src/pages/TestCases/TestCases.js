import React, { Component } from 'react';
import CustomTable from "./components/UserTable/CustomTable";

export default class TestCases extends Component {
    constructor(props) {
        super(props);
        this.state = {};
    }

    render() {
        return (
            <div className="test-cases-page">
                <CustomTable />
            </div>
        );
    }
}
