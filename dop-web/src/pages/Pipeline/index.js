import Pipeline from './Pipeline';
import PipelineInfo from './PipelineInfo/PipelineInfo'
import PipelineProject from './PipelineProject/PipelineProject'
import PipelineTest from './test/test'
import BasicLayout from "../../layouts/BasicLayout";
import EditPipelineInfo from "./PipelineInfo/EditPipelineInfo";


const pipelineConfig = [
    //pipeline
    {
        path: '/pipeline',
        layout: BasicLayout,
        component: Pipeline
    },
    {
        path: '/pipeline/new',
        layout: BasicLayout,
        component: PipelineInfo
    },
    {
        path: '/pipeline/project/:id',
        layout: BasicLayout,
        component: PipelineProject
    }, {
        path: '/pipeline/edit/:id',
        layout: BasicLayout,
        component: EditPipelineInfo
    },{
        path: '/pipeline/test',
        layout: BasicLayout,
        component: PipelineTest
    },
];

export {Pipeline, PipelineInfo, PipelineProject, PipelineTest, pipelineConfig};
