import React, { Component } from 'react';
import AuthorityTable from './components/AuthorityTable';

export default class ExecuteLogs extends Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    return (
      <div className="execute-logs-page">
        <AuthorityTable />
      </div>
    );
  }
}
