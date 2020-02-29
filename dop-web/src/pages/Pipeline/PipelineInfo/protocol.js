//流水线是否自带jenkinsfile
const jenkinsFile = ["自带Jenkinsfile", "无Jenkinsfile"];
// 0---自带jenkinsfile
// 1---无jenkinsfile

//流水线触发方式
const monitor = ["自动触发","手动触发","定时触发"];
// 0---自动触发
// 1---手动触发
// 2---定时触发

//流水线定时触发间隔时长
const timing =["10分钟", "20分钟", "30分钟", "60分钟", "120分钟", "240分钟", "360分钟"];

//可选择的任务
const availableSteps = ["拉取代码", "构建maven", "构建node", "构建djanggo", "构建docker镜像", "推送docker镜像", "自定义脚本", "部署"],
