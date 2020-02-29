import React, {Component} from 'react';
import { FormattedMessage} from 'react-intl';
const permissionAsideMenuConfig=[
    {
        name: <FormattedMessage id="permission.home" defaultMessage="首页"/>,
        path: '/',
        icon: 'home2'
    },
    {
        name: <FormattedMessage id="permission.pointManagement" defaultMessage="功能点管理"/>,
        path: '/permission/permissions',
        icon: 'box' ,
    },
    {
        name: <FormattedMessage id="permission.roleManagement" defaultMessage="角色管理"/>,
        path: '/permission/roles',
        icon: 'account',
    },

    {
        name: <FormattedMessage id="permission.userManagement" defaultMessage="用户管理"/>,
        path: '/permission/roles/userwithrole',
        icon: 'account-filling',
    },
    {
        name: <FormattedMessage id="permission.dataPermission" defaultMessage="数据权限"/>,
        path: '/permission/dataRules',
        icon: 'filter',
    },

]
export {permissionAsideMenuConfig};