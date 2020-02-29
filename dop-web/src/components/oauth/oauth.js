/**
 *  本js用于oauth认证
 *  @author 张富利
 *
 * */

import API from '../../pages/API'
import UUIDV4 from 'uuid/v4'
import CryptoJS from 'crypto-js'
import Axios from 'axios'
import Qs from 'qs'


/**
 *  获取加密后的签名
 *  @param clientSecret 服务器端颁发
 *  @param URI 请求路径
 *  @param method 请求方法
 *  @param obj 请求参数的object(不含signature)
 *  @return code 加密后的签名
 *
 * */

function signature(clientSecret, URI, method, obj) {
    let newkey = Object.keys(obj).sort();
    let newObj = {};
    for (let i = 0; i < newkey.length; i++)
        newObj[newkey[i]] = obj[newkey[i]];

    let code = URI + method;
    Object.entries(newObj).forEach(([key, value]) => {
        code += key + '=' + value + '&';
    });
    code =  code.substring(0, code.length - 1);

    let base64 = CryptoJS.enc.Base64.stringify(CryptoJS.HmacSHA256(
        code,
        clientSecret
    )).toString();

    let base64url = base64.split('+').join('-').split('/').join('_');
    return base64url
}

/**
 *  用于获取服务器端的时间戳，并调用oauth
 *  @param app App作用域
 *
 * */

function getTimeStamp() {
    let path = "/v1/time/epoch";
    let pre = new Date().getTime();
    return new Promise((resolve, reject)=>{
        Axios.get(API.gateway + path).then((response) => {
            oauth(response.data + new Date().getTime() - pre);
            resolve(response.data + new Date().getTime() - pre)
        }).catch((error) => {
            reject(error)
        });
    })


}
/**
 *  用于请求服务器获取accesstoken，并保存在App的参数里面
 *  @param timestamp 服务器时间戳
 *
 * */

function oauth(timestamp) {
    const clientID = "softeng_dop_webBkAb3YKcAdPgRjb";
    const clientSecret = "c7M5ZV75J6wXDyuQ0X5XqleuGwJlh1Xq1muPLbpW6jF__hL0xKS1pFmaIhMENH8e";
    let URI = "/v1/oauth/token";
    let data = {
        grant_type: "client_credentials",
        client_id: clientID,
        timestamp: timestamp,
        nouce: UUIDV4()
    };

    data = {...data, signature: signature(clientSecret, URI, "POST", data)};
    return new Promise((resolve, reject)=>{
        Axios.post(API.gateway + URI, Qs.stringify(data)).then((response)=>{
            Axios.defaults.headers.common['Authorization'] = "Bearer " + response.data.access_token;
            window.sessionStorage.setItem("Authorization", response.data.access_token);
            // window.sessionStorage.setItem('language', navigator.language);
            resolve(response.data.access_token)
        }).catch((error)=>{
            reject(error)
        })
    })
}


export {getTimeStamp, oauth}

