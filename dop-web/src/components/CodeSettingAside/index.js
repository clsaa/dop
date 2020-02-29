/* eslint no-undef:0, no-unused-expressions:0, array-callback-return:0 */
import React, { Component } from 'react';
import Menu, { Item as MenuItem } from '@icedesign/menu';
import { withRouter, Link } from 'react-router-dom';
import { Icon } from '@icedesign/base';
import {FormattedMessage} from 'react-intl';

import './index.scss';


import imgBack from './imgs/back.png';
import imgEdit from './imgs/edit.png';
import imgProtect from './imgs/protect.png';
import imgMembers from './imgs/members.png';

// @withRouter

class CodeSettingProjectAside extends Component {
    static propTypes = {};

    static defaultProps = {};


    constructor(props) {
        super(props);
        const {username,projectname}=this.props.match.params;
        this.state={
            username:username,
            projectname:projectname,
            projectid:username+"/"+projectname,
        };
    }


    render() {
        const { location } = this.props;
        const { pathname } = location;
        const { username,projectname} = this.state;


        const backLink="/code/"+username+"/"+projectname;
        const editLink="/code/"+username+"/"+projectname+"/edit";
        const protectBranchLink="/code/"+username+"/"+projectname+"/protected_branches";
        const memberLink="/code/"+username+"/"+projectname+"/project_members";



        return (
            <Menu mode="inline" selectedKeys={[pathname]} className="ice-menu-custom">
                <MenuItem key={backLink}>
                    <Link to={backLink} className="ice-menu-link">
                        <img src={imgBack}/>
                        <span className="ice-menu-item-text"><FormattedMessage id="code.settingaside.back"/></span>
                    </Link>
                </MenuItem>
                <MenuItem key={editLink}>
                    <Link to={editLink} className="ice-menu-link">
                        <img src={imgEdit}/>
                        <span className="ice-menu-item-text"><FormattedMessage id="code.settingaside.basicinfo"/></span>
                    </Link>
                </MenuItem>
                <MenuItem key={protectBranchLink}>
                    <Link to={protectBranchLink} className="ice-menu-link">
                        <img src={imgProtect}/>
                        <span className="ice-menu-item-text"><FormattedMessage id="code.settingaside.protectbranch"/></span>
                    </Link>
                </MenuItem>
                <MenuItem key={memberLink}>
                    <Link to={memberLink} className="ice-menu-link">
                        <img src={imgMembers}/>
                        <span className="ice-menu-item-text"><FormattedMessage id="code.settingaside.members"/></span>
                    </Link>
                </MenuItem>
            </Menu>
        );
    }
}

export default withRouter(CodeSettingProjectAside)
