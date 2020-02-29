import React, { Component } from 'react';
import Axios from 'axios';
import API from '../../pages/API';

class AuthRequire extends Component{

    constructor(props) {
        super(props);;
    }
    permissionNamesHas=(permissionList,permissionName)=>
    {
       if(permissionList.indexOf(permissionName)!=-1)
       {
           console.log("拥有功能点"+permissionName)
           return true
       }
           console.log("没有功能点"+permissionName)
       return false
    }
    render(){
        const {permissionList,permissionName, children} = this.props;
        return this.permissionNamesHas(permissionList,permissionName) ? children : null;
    }
}
export default AuthRequire;