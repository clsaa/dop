import React, {Component} from 'react';
import '../Styles.scss'
import {Input, Select, Form, Loading} from '@icedesign/base';
import {Feedback} from "@icedesign/base/index";
import {injectIntl} from "react-intl";

const {Combobox} = Select;
const {toast} = Feedback;
const FormItem = Form.Item;

class Shell extends Component {
    constructor(props) {
        super(props);
        this.state = {
            visible: false,
            myScript: ["shell"]
        }
    }

    buildShell(value) {
        this.props.onShellChange(value)
    }

    render() {
        const formItemLayout = {
            labelCol: {
                span: 10
            },
            wrapperCol: {
                span: 10
            }
        };
        return (
            <Loading shape="fusion-reactor" visible={this.state.visible}>
                <h3 className="chosen-task-detail-title">{this.props.intl.messages["pipeline.info.step.shell.title"]}</h3>
                <Form
                    labelAlign="left"
                    labelTextAlign="left"
                >
                    <FormItem
                        label={this.props.intl.messages["pipeline.info.step.shell.type"]+ ": "}
                        {...formItemLayout}
                    >
                        <Combobox
                            style={{'width': '200px'}}
                            filterLocal={false}
                            placeholder={this.props.intl.messages["pipeline.info.step.shell.type.placeholder"]}
                            dataSource={this.state.myScript}
                        />
                    </FormItem>
                    <FormItem
                        label={this.props.intl.messages["pipeline.info.step.shell.content"]+ ": "}
                        {...formItemLayout}
                        required
                    >
                        <Input
                            multiple
                            value={this.props.shell}
                            className="area"
                            onChange={this.buildShell.bind(this)}
                            style={{'width': '200px'}}
                        />
                    </FormItem>
                </Form>
            </Loading>
        )
    }
}

export default injectIntl(Shell)
