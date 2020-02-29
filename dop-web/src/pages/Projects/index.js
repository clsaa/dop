import Projects from './Projects.js';
import BasicLayout from "../../layouts/BasicLayout";
import ProjectDetail from './components/projectManagement/ProjectDetail/ProjectDetail';
import ApplicationDetail from './ApplicationDetail/ApplicationDetail';
import ApplicationEnvironmentDetail
    from './components/ApplicationManagement/ApplicationEnvironmentDetail/ApplicationEnvironmentDeatil'


const projectConfig = [
    {
        path: '/project',
        layout: BasicLayout,
        component: Projects,
    }, {
        path: '/projectDetail',
        layout: BasicLayout,
        component: ProjectDetail,
    },
    {
        path: '/applicationDetail',
        layout: BasicLayout,
        component: ApplicationDetail,
    },
    {
        path: '/application/environment/detail',
        layout: BasicLayout,
        component: ApplicationEnvironmentDetail
    }
];
export {projectConfig};
