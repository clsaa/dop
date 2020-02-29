import Repos from './ReposManagement/Repos';
import BasicLayout from '../../layouts/BasicLayout';
import Image from './Image';
import ImageList from "./ImageList/ImageList";
import NamespaceLogList from "./NamespaceLog/NamespaceLogList";
import ImageInfo from './ImageInfo'

const imageConfig =[
    {
        path: '/image/projects/:projectId/repos',
        layout: BasicLayout,
        component: Repos,
    },
    {
        path: '/image/projects',
        layout: BasicLayout,
        component: Image,
    },
    {
        path: '/repos/:projectName/:repoName/images',
        layout: BasicLayout,
        component: ImageList
    },
    {
        path: '/image/projects/:projectId/logs',
        layout: BasicLayout,
        component: NamespaceLogList
    },
    {
        path:'/repos/:project/:repo/images/:tag',
        layout: BasicLayout,
        component: ImageInfo
    }
];

export {Repos,imageConfig,Image,ImageList,NamespaceLogList}