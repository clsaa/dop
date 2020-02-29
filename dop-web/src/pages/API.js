// 配置所有接口的API文件

// const host = "http://open.dop.clsaa.com";
const host = "http://localhost:8888";
const pipeline = "/pipeline-server";
const application = "/application-server";
const permission = "/permission-server";
const code = "/code-server";
const user = "/user-server";
const image = "/image-server";
const test = "/test-server";

const API = {
    address: "http://www.dop.clsaa.com/#/",
    gateway: host,
    pipeline: host + pipeline,
    application: host + application,
    permission: host + permission,
    code: host + code,
    user: host + user,
    image: host + image,
    test: host + test
};

export default API;
