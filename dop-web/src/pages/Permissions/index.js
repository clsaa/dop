import Permission from './Permissions/Permission';
import BasicLayout from "../../layouts/BasicLayout";
import DataRules from "./DataRules/DataRules";
import Role from "./Roles/Role";
import UserRoleMapping from "./Roles/UserRoleMapping/UserRoleMapping";
import PermissionLayout from "../../layouts/PermissionLayout";

const permissionConfig = [
    //permission
    {
        path: '/permission/permissions',
        layout: PermissionLayout,
        component: Permission,
    },

    {
        path: '/permission/dataRules',
        layout: PermissionLayout,
        component: DataRules,
    },

    //role
    {
        path: '/permission/roles',
        layout: PermissionLayout,
        component: Role,
    },
    {
        path: '/permission/roles/userwithrole',
        layout: PermissionLayout,
        component: UserRoleMapping,
    },
];

export {Permission,permissionConfig};