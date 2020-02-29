import React, {Component} from 'react';

import {Radio} from "@icedesign/base";
import {injectIntl} from "react-intl";

const {Group: RadioGroup} = Radio;

/**
 *    开发模式按钮
 *
 * */
class ProductModeController extends Component {
    constructor(props) {
        super(props);


        //与Filed组件通信
        this.state = {
            value: typeof props.value === "undefined" ? [] : props.value
        };

        this.onChange = this.onChange.bind(this);
    }


    componentWillReceiveProps(nextProps) {
        if ("value" in nextProps) {
            this.setState({
                value: typeof nextProps.value === "undefined" ? [] : nextProps.value
            });
        }
    }


    onChange(value) {
        this.setState({
            value: value
        });
        this.props.onChange(value);
    }

    render() {
        const list = [
            {
                value: "FREE",
                label: this.props.intl.messages['projects.radio.free']
            },
            {
                value: "BRANCH",
                label: this.props.intl.messages['projects.radio.branch']
            },
        ];
        return (
            <div>
                <RadioGroup
                    dataSource={list}
                    value={this.state.value}
                    onChange={this.onChange}
                />
            </div>
        );
    }

}

export default injectIntl(ProductModeController)