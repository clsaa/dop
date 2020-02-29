import React, { Component } from 'react';
import { Button, Input, Select } from '@icedesign/base';
import {injectIntl} from "react-intl";

class TableFilter extends Component {
  static displayName = 'TableFilter';

  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    return (
      <div style={styles.tableFilter}>
        <div style={styles.title}>{this.props.intl.messages['test.exeLogs.search.title']}</div>
        <div style={styles.filter}>
          <div style={styles.filterItem}>
            <span style={styles.filterLabel}>{this.props.intl.messages['test.exeLogs.search.testManager']}</span>
            <Input />
          </div>
          <div style={styles.filterItem}>
            <span style={styles.filterLabel}>{this.props.intl.messages['test.exeLogs.search.status']}</span>
            <Select style={{ width: '200px' }} placeholder={this.props.intl.messages['test.exeLogs.search.status.place']}>
              <Select.Option value="all">{this.props.intl.messages['test.exeLogs.search.status.all']}</Select.Option>
              <Select.Option value="checked">{this.props.intl.messages['test.exeLogs.search.status.success']}</Select.Option>
              <Select.Option value="unCheck">{this.props.intl.messages['test.exeLogs.search.status.fail']}</Select.Option>
            </Select>
          </div>
          <Button type="primary" style={styles.submitButton}>
            {this.props.intl.messages['test.exeLogs.search']}
          </Button>
        </div>
      </div>
    );
  }
}

const styles = {
  tableFilter: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: '20px',
    marginBottom: '20px',
    background: '#fff',
  },
  title: {
    height: '20px',
    lineHeight: '20px',
    color: '#333',
    fontSize: '18px',
    fontWeight: 'bold',
    paddingLeft: '12px',
    borderLeft: '4px solid #666',
  },
  filter: {
    display: 'flex',
  },
  filterItem: {
    display: 'flex',
    alignItems: 'center',
    marginLeft: '20px',
  },
  filterLabel: {
    fontWeight: '500',
    color: '#999',
  },
  submitButton: {
    marginLeft: '20px',
  },
};
export default injectIntl(TableFilter);
