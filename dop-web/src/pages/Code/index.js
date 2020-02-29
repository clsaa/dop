import {ProjectList} from './ProjectList'
import ProjectOverview from './ProjectOverview'
import NewProject from './NewProject'
import Tree from './Tree'
import CodeLayout from "../../layouts/CodeLayout";
import CodeProjectLayout from '../../layouts/CodeProjectLayout';
import CodeSettingLayout from '../../layouts/CodeSettingLayout';
import Blob from "./Blob";
import EditFile from './EditFile'
import CommitList from './CommitList';
import FilePathList from './FilePathList';
import Commit from './Commit';
import EditProject from './EditProject';
import SSH from './SSH';
import NewSSH from './NewSSH'
import SSHREADME from './SSHREADME'
import BranchList from './BranchList'
import NewBranch from './NewBranch'
import TagList from './TagList'
import NewTag from './NewTag'
import ProtectBranch from './ProtectBranch'
import MemberList from './MemberList'
import MergeRequestList from './MergeRequestList'
import NewMergeRequest from './NewMergeRequest'
import MergeRequest from './MergeRequest'


const codeConfig = [

    {
        path: '/code/projects/new',
        layout: CodeLayout,
        component: NewProject
    },
    {
        path: '/code/projects/:sort',
        layout: CodeLayout,
        component: ProjectList
    },
    {
        path: '/code/ssh',
        layout: CodeLayout,
        component: SSH
    },
    {
        path: '/code/ssh/README',
        layout: CodeLayout,
        component: SSHREADME
    },
    {
        path: '/code/ssh/new',
        layout: CodeLayout,
        component: NewSSH
    },
    {
        path: '/code/:username/:projectname',
        layout: CodeProjectLayout,
        component: ProjectOverview

    },
    {
        path: '/code/:username/:projectname/tree/:ref/:path',
        layout: CodeProjectLayout,
        component: Tree
    },
    {
        path: '/code/:username/:projectname/blob/:ref/:path',
        layout: CodeProjectLayout,
        component: Blob
    },
    {
        path: '/code/:username/:projectname/edit/:ref/:path',
        layout: CodeProjectLayout,
        component: EditFile
    },
    {
        path: '/code/:username/:projectname/commitlist/:ref',
        layout: CodeProjectLayout,
        component: CommitList
    },
    {
        path: '/code/:username/:projectname/filepathlist/:ref',
        layout: CodeProjectLayout,
        component: FilePathList
    },
    {
        path: '/code/:username/:projectname/commit/:sha',
        layout: CodeProjectLayout,
        component: Commit
    },
    {
        path: '/code/:username/:projectname/branches',
        layout: CodeProjectLayout,
        component: BranchList
    },
    {
        path: '/code/:username/:projectname/branches/new',
        layout: CodeProjectLayout,
        component: NewBranch
    },
    {
        path: '/code/:username/:projectname/tags',
        layout: CodeProjectLayout,
        component: TagList
    },
    {
        path: '/code/:username/:projectname/tags/new',
        layout: CodeProjectLayout,
        component: NewTag
    },
    {
        path: '/code/:username/:projectname/edit',
        layout: CodeSettingLayout,
        component: EditProject
    },
    {
        path: '/code/:username/:projectname/protected_branches',
        layout: CodeSettingLayout,
        component: ProtectBranch
    },
    {
        path: '/code/:username/:projectname/project_members',
        layout: CodeSettingLayout,
        component: MemberList
    },
    {
        path: '/code/:username/:projectname/merge_requests/new',
        layout: CodeProjectLayout,
        component: NewMergeRequest
    },
    {
        path: '/code/:username/:projectname/merge_requests',
        layout: CodeProjectLayout,
        component: MergeRequestList
    },
    {
        path: '/code/:username/:projectname/merge_requests/:iid',
        layout: CodeProjectLayout,
        component: MergeRequest
    }


];

export default codeConfig;
