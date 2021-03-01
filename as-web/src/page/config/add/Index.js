import React from 'react';
import './Index.less'
import {Input, Button, Alert, Table, Popconfirm} from 'antd';
import api from '../../../service';

const {TextArea} = Input;
const PAGE_SIZE = 6;
export default class Index extends React.Component {
    columns = [
        {
            title: 'version',
            dataIndex: 'version',
        },
        {
            title: 'createTime',
            dataIndex: 'createTime'
        },
        {
            title: 'operation',
            dataIndex: 'operation',
            render: (text, record) => {
                const style = record.version === this.version ? {color: 'gray'} : null;
                return <Popconfirm
                    title={`确定要还原到${record.version}版本`}
                    disabled={record.version === this.version}
                    onConfirm={() => this.restConfig(record)}
                >
                    <a style={style}>还原</a>
                </Popconfirm>
            }
        }
    ];
    state = {
        configText: null,
        submit: false,
        visibleAlert: false,
        errorMsg: null
    };

    constructor(props) {
        super(props);
        const {location: {state: {item} = {}} = {}} = this.props;
        const {namespace, originalUrl, jsonUrl, version} = item || {};
        this.namespace = namespace;
        this.version = version;
        this.listDisplay = !!namespace;
        this.loadConfigData(originalUrl);
    }

    componentDidMount() {
        this.loadData();
    }

    restConfig = record => {
        const {originalUrl} = record;
        this.loadConfigData(originalUrl);
    };
    onSubmit = () => {
        if (!this.namespace) {
            this.setState({
                visibleAlert: true,
                errorMsg: 'namespace不能为空'
            });
            return;
        }
        const {configText} = this.state;
        api.updateConfig({config: configText, namespace: this.namespace})()
            .then(response => response.json())
            .then(result => {
                const {code, data: {version}} = result;
                if (code === 0) {
                    this.setState({
                        visibleAlert: true,
                        errorMsg: null,
                        submit: false
                    });
                    this.version = version;
                    this.configText = configText;
                    this.loadData();
                }
            })
            .catch(e => {
                this.setState({
                    visibleAlert: true,
                    errorMsg: e.toString()
                })
            })
    };

    loadConfigData(originalUrl) {
        if (!originalUrl) return;
        fetch(originalUrl)
            .then(res => res.text())
            .then(txt => {
                if (this.configText == null) {
                    this.configText = txt;
                }
                const submit = this.configText !== txt;
                this.setState({
                    configText: txt,
                    submit
                })
            }).catch(e => {
                console.log(e);
            }
        );
    }

    loadData() {
        if (!this.namespace) {
            return;
        }
        api.getConfig({namespace: this.namespace, pageIndex: 1, pageSize: PAGE_SIZE})
            .then(res => res.json())
            .then(result => {
                const {data: {list, total} = {}} = result;
                this.setState({
                    list,
                    total
                })
            }).catch(e => {
            console.log(e);
        })
    }

    render() {
        const {configText, visibleAlert, errorMsg, list, submit} = this.state;
        return <div className='config-add'>
            <div className='config-add-left'>
                <div className='config-add-top'>
                    <div>namespace：</div>
                    <TextArea
                        onChange={({target: {value}}) => {
                            this.namespace = value;
                        }}
                        defaultValue={this.namespace}
                        placeholder='请输入...'
                        autoSize={{maxRows: 1}}
                        disabled={this.namespace != null}
                    />
                </div>
                <TextArea
                    className='config-add-input'
                    value={configText}
                    onChange={({target: {value}}) => {
                        const submit = this.configText !== value;
                        this.setState({
                            configText: value,
                            submit
                        })
                    }}
                    placeholder='请输入...'
                    autoSize={{minRows: 6, maxRows: 16}}
                />
                <div className='config-add-btn'>
                    <Popconfirm
                        title='确定要发布吗？'
                        disabled={!submit}
                        onConfirm={() => this.onSubmit()}
                    >
                        <Button
                            type='primary'
                            disabled={!submit}
                        >
                            提交发布
                        </Button>
                    </Popconfirm>
                </div>
                {visibleAlert ? (<Alert message={errorMsg ? errorMsg : '发布成功'} type={errorMsg ? 'error' : 'success'}
                                        closable/>) : null}
            </div>
            {this.listDisplay ? <Table
                className='config-add-right'
                columns={this.columns}
                dataSource={list}
                rowKey={item => item.id}
                pagination={false}
                size='small'
                footer={() => <div>支持还原到最近的5次发布</div>}
            /> : null}
        </div>
    }

}
