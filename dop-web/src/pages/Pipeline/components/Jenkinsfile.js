import React, {Component} from 'react';
import {FormBinderWrapper, FormBinder, FormError} from '@icedesign/form-binder';
import {Input} from '@icedesign/base';
import './Styles.scss'


export default class Jenkinsfile extends Component{
    formChange = value => {
        this.props.onChange(value);
    };
    render(){
        return (
            <FormBinderWrapper
                value={this.props.jenkinsfile}
                onChange={this.formChange}
                ref="jenkinsfile"
            >
                <div>
                    <div className="form-item">
                        <span className="form-item-label">仓库地址: </span>
                        <FormBinder name="git" required message="请输入仓库地址">
                            <Input
                                placeholder="请输入仓库地址"
                                value={this.props.jenkinsfile.git}
                                className = "combobox"
                            />
                        </FormBinder>
                        <FormError className="form-item-error" name="git"/>
                    </div>
                    <div className="form-item">
                        <span className="form-item-label">Jenkinsfile<br />路径: </span>
                        <FormBinder name="path" required message="请输入Jenkinsfile路径">
                            <Input
                                placeholder="请输入Jenkinsfile路径"
                                value={this.props.jenkinsfile.path?this.props.jenkinsfile.path:"./Jenkinsfile"}
                                className = "combobox"
                            />
                        </FormBinder>
                        <FormError className="form-item-error" name="path"/>
                    </div>
                </div>
            </FormBinderWrapper>
        )
    }
}
