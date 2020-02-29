import React, {Component} from 'react';
import {Route, withRouter} from 'react-router-dom';
import LoginTransfer from '../pages/Login/Transfer/LoginTransfer'
import Axios from "axios/index";

class PrivateRoute extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isAuthenticated: !!window.sessionStorage.getItem("x-login-token")
        }
    }

    componentWillMount() {
        if(!this.state.isAuthenticated){
            const {history} = this.props;
            setTimeout(() => {
                history.replace("/login");
            }, 1000)
        }else{
            Axios.defaults.headers.common['Authorization'] = "Bearer " + window.sessionStorage.getItem("Authorization");
            Axios.defaults.headers.common['x-login-token'] = window.sessionStorage.getItem("x-login-token");
            Axios.defaults.headers.common['x-login-user'] = window.sessionStorage.getItem("x-login-user");
        }
    }

    render() {
        let { ...rest} = this.props;
        return  this.state.isAuthenticated ?
            (<Route
                key={this.props.routePath}
                {...rest}
            /> ) :
            (<LoginTransfer />)

    }
}

export default withRouter(PrivateRoute);
