import Login from './Login/Login'
import Register from './Register/Register';
import RegisterTransfer from './Transfer/RegisterTransfer';
import LoginTransfer from './Transfer/LoginTransfer';
import ModifyPwd from './ModifyPwd/ModifyPwd';
import {getTimeStamp, oauth} from '../../components/oauth/oauth'
import base64url from 'base64url'
import API from "../API";
import Axios from "axios/index";

const JSEncrypt = require('../../../node_modules/jsencrypt/bin/jsencrypt');

function RSA() {
    return new Promise((resolve, reject) => {
        getTimeStamp().then((data) => {
            return oauth(data)
        }).then((data) => {
            resolve(data)
        }).catch((error) => {
            reject(error)
        })
    })
}

function Encryption(data, publicKey){
    let encrypt  = new JSEncrypt.JSEncrypt();
    encrypt.setPublicKey(base64url.toBase64(publicKey));
    return base64url.fromBase64(encrypt.encrypt(data));
}

function PublicKey() {
    let url = API.gateway + '/user-server/v1/account/RSAPublicKey';
    return new Promise((resolve, reject) => {
        Axios.get(url).then((response) => {
            resolve(response.data)
        }).catch((error) => {
            reject(error)
        })
    })
}


const loginConfig = [
    {
        path: '/login',
        component: Login,
        isLogin: true
    },
    {
        path: '/register',
        component: Register,
        isLogin: true
    },
    {
        path: '/register/transfer',
        component: RegisterTransfer,
        isLogin: true
    },
    {
        path: '/modifyPwd',
        component: ModifyPwd,
        isLogin: true
    }
];


export {Login, Register, RegisterTransfer, ModifyPwd, LoginTransfer, RSA, Encryption, PublicKey, loginConfig};
