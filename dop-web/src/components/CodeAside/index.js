/* eslint no-undef:0, no-unused-expressions:0, array-callback-return:0 */
import React, { Component } from 'react';
import Menu, { Item as MenuItem } from '@icedesign/menu';
import { withRouter, Link } from 'react-router-dom';
import FoundationSymbol from 'foundation-symbol';
import { Icon } from '@icedesign/base';
import {FormattedMessage} from 'react-intl';

import './index.scss';

// @withRouter

class CodeAside extends Component {
  static propTypes = {};

  static defaultProps = {};


  constructor(props) {
      super(props);
  }


  render() {
    const { location } = this.props;
    const { pathname } = location;

    const backLink="/project";

    const projectsLink="/code/projects/personal";

    const groupsLink="/code/groups";

    const sshLink="/code/ssh";


    return (
        <Menu mode="inline" selectedKeys={[pathname]} className="ice-menu-custom">
            <MenuItem key={backLink}>
                <Link to={backLink} className="ice-menu-link">
                    <FoundationSymbol size="small" type="home2" >
                        <Icon size="small" type="home2" />
                    </FoundationSymbol>
                    <span className="ice-menu-item-text"><FormattedMessage id="code.aside.home"/></span>
                </Link>
            </MenuItem>
            <MenuItem key={projectsLink}>
                <Link to={projectsLink} className="ice-menu-link">
                        <FoundationSymbol size="small" type="cascades" >
                            <Icon size="small" type="cascades" />
                        </FoundationSymbol>
                    <span className="ice-menu-item-text"><FormattedMessage id="code.aside.projects"/></span>
                </Link>
            </MenuItem>
            {/*<MenuItem key={groupsLink}>*/}
                {/*<Link to={groupsLink} className="ice-menu-link">*/}
                    {/*<FoundationSymbol size="small" type="fans" >*/}
                        {/*<Icon size="small" type="fans" />*/}
                    {/*</FoundationSymbol>*/}
                    {/*<span className="ice-menu-item-text">{"小组"}</span>*/}
                {/*</Link>*/}
            {/*</MenuItem>*/}
            <MenuItem key={sshLink}>
                <Link to={sshLink} className="ice-menu-link">
                    <FoundationSymbol size="small" type="key" >
                        <Icon size="small" type="key" />
                    </FoundationSymbol>
                    <span className="ice-menu-item-text"><FormattedMessage id="code.aside.sshkey"/></span>
                </Link>
            </MenuItem>
        </Menu>
    );
  }
}

export default withRouter(CodeAside)
