import React, {Component} from 'react';
import { FormattedMessage} from 'react-intl';
const testMenuConfig=[
    {
        name: <FormattedMessage id="permission.home" defaultMessage="首页"/>,
        path: '/',
        icon: 'home2'
    },
    {
        name: <FormattedMessage id="test.menu.caseManage" defaultMessage="用例管理"/>,
        path: '/testCases',
        icon: 'repair' ,
    },
    {
        name: <FormattedMessage id="test.menu.groupManage" defaultMessage="分组管理"/>,
        path: '/test/testGroups',
        icon: 'box',
    },
    {
        name: <FormattedMessage id="test.menu.newGroup" defaultMessage="新建分组"/>,
        path: '/test/createGroup',
        icon: 'add',
    },
];
export {testMenuConfig};
