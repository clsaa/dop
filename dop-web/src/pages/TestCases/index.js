import TestCases from "./TestCases";
import CreateInterfaceCase from "./CreateInterfaceCase";
import CreateManualCaseFrom from "./components/CreateTestCases/CreateManualCaseForm";
import AuthorityTable from "./ExecuteLogs/components/AuthorityTable";
import CreateInterfaceScripts from "./CreateInterfaceScripts";
import EditCaseInfo from "./EditCaseInfo";
import TestLayout from "../../layouts/TestLayout";
import GroupTable from "./components/GroupTable/GroupTable";
import CreateGroup from "./CreateGroup";
import EditGroup from "./EditGroup";
import GroupLogTable from "./GroupLogs/GroupLogTable";
import DetailCaseLogTable from "./GroupLogs/DetailCaseLogTable";

const testConfig = [
    {
        path: "/test/groupLogs/detail/:groupLogId/:groupId",
        layout: TestLayout,
        component: DetailCaseLogTable,
        isLogin: true
    },
    {
        path: "/test/groupLogs/:groupId",
        layout: TestLayout,
        component: GroupLogTable,
        isLogin: true
    },
    {
        path: "/test/editGroups/:groupId",
        layout: TestLayout,
        component: EditGroup,
        isLogin: true
    },
    {
        path: "/test/createGroup",
        layout: TestLayout,
        component: CreateGroup,
        isLogin: true
    },
    {
        path: "/test/testGroups",
        layout: TestLayout,
        component: GroupTable,
        isLogin: true
    },
    {
        path: "/test/editCases/:caseId",
        layout: TestLayout,
        component: EditCaseInfo,
        isLogin: true
    },
    {
        path: "/test/showExecuteLogs/:caseId",
        layout: TestLayout,
        component: AuthorityTable,
        isLogin: true
    },
    {
        path: "/test/createInterfaceScripts/:caseId",
        layout: TestLayout,
        component: CreateInterfaceScripts,
        isLogin: true
    },
    {
        path: "/test/createInterfaceCase",
        layout: TestLayout,
        component: CreateInterfaceCase,
        isLogin: true
    },
    {
        path: "/test/createManualCase",
        layout: TestLayout,
        component: CreateManualCaseFrom,
        isLogin: true
    },
    {
        path: "/testCases",
        layout: TestLayout,
        component: TestCases,
        isLogin: true
    },
];


export {testConfig};
