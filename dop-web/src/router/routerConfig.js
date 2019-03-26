// 以下文件格式为描述路由的协议格式
// 你可以调整 routerConfig 里的内容
// 变量名 routerConfig 为 iceworks 检测关键字，请不要修改名称

import BasicLayout from '../layouts/BasicLayout';
import {Projects, Application, ApplicationDetail} from '../pages/Projects';
import {ApplicationEnvironmentDetail} from '../pages/Projects';
import {pipelineConfig} from '../pages/Pipeline';
import NotFound from '../pages/NotFound';
import {loginConfig} from '../pages/Login'
import Ciadjust from '../pages/Ciadjust';
import {Code,ProjectList,ProjectOverview,NewProject} from '../pages/Code'
import CodeLayout from '../layouts/CodeLayout';
import {permissionConfig} from "../pages/Permissions";
import {roleConfig} from "../pages/Roles";
import {dataRulesConfig} from "../pages/DataRules";

const baseConfig = [
    {
        path: '*',
        layout: BasicLayout,
        component: NotFound,
    },
];


const routerConfig = [ ...pipelineConfig, ...loginConfig, ...permissionConfig, ...roleConfig, ...dataRulesConfig, ...baseConfig];

export default routerConfig;
