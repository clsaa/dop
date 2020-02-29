import React, { Component } from 'react';
import TableFilter from './TableFilter';
import CustomTable from './CustomTable';

export default class AuthorityTable extends Component {
  static displayName = 'AuthorityTable';

  static propTypes = {};

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {
      caseId: this.props.match.params.caseId
    };
  }

  render() {
    return (
      <div style={styles.container}>
        <TableFilter />
        <CustomTable caseId={this.state.caseId}/>
      </div>
    );
  }
}

const styles = {};
