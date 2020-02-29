// 菜单配置
// headerMenuConfig：头部导航配置
// asideMenuConfig：侧边导航配置
import React from 'react';
import {FormattedMessage} from 'react-intl';


const headerMenuConfig = [
    {
        name:  <FormattedMessage id="base.feedback" defaultMessage="反馈"/>,
        path: 'https://github.com/clsaa/dop/issues',
        external: true,
        newWindow: true,
        icon: 'message',
    },
    {
        name: <FormattedMessage id="base.help" defaultMessage="帮助"/>,
        path: 'mailto:812022339@qq.com',
        external: true,
        newWindow: true,
        icon: 'bangzhu',
    },
];

const asideMenuConfig = [
    {
        name: <FormattedMessage id="project.projectName" defaultMessage="全部项目"/>,
        path: '/project',
        icon: 'home2',
    },
    {
        name: <FormattedMessage id="pipeline.name" defaultMessage="流水线管理"/>,
        path: '/pipeline',
        icon: 'ol-list',
    },
    {
        name: <FormattedMessage id="test.name" defaultMessage="测试管理"/>,
        path: '/testCases',
        icon: 'repair',
    },
    {

        name: <FormattedMessage id="permission.permissionManagement" defaultMessage="权限管理"/>,
        path: '/permission/permissions',
        icon: 'account' ,
    },

    {
        name: <FormattedMessage id="code.name" defaultMessage="代码管理"/>,
        path: '/code/projects/personal',
        icon: 'code',
    },
    {
        name: <FormattedMessage id="image.name" defaultMessage="镜像管理"/>,
        path: '/image/projects',
        icon: 'image',
    }


];

export {headerMenuConfig, asideMenuConfig};
