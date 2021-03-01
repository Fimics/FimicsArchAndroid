import React from 'react';
import {Table, Switch, Popconfirm} from 'antd';
import './Index.less'
import api from '../../service'

const PAGE_SIZE = 2;
export default class Index extends React.Component {
    state = {
        data: [],
        total: 0,
        loading: false
    };

    constructor(props) {
        super(props);
        // {"uid":"1","imoocId":"111","orderId":"222","userName":"Rose","forbid":"1"}
        this.columns = [
            {
                title: 'uid',
                dataIndex: 'uid'
            },
            {
                title: "imoocId",
                dataIndex: 'imoocId'
            },
            {
                title: '用户名',
                dataIndex: "userName"
            },
            {
                title: "创建时间",
                dataIndex: 'createTime'
            },
            {
                title: "forbid",
                dataIndex: 'forbid',
                render: (text, record) => {
                    return <Popconfirm
                        title={`确定要${record.forbid === '1' ? '解禁' : '冻结'}?`}
                        onConfirm={() => this.toggleForbid(record)}
                    >
                        <Switch
                            checkedChildren="正常"
                            unCheckedChildren="冻结"
                            checked={text !== "1"}
                        />
                    </Popconfirm>
                },
                width: '20%'
            }
        ];
    }

    render() {
        const {data, total, loading} = this.state;
        return (
            <Table
                loading={loading}
                rowKey={item => item.uid}
                dataSource={data}
                pagination={
                    {
                        total,
                        pageSize: PAGE_SIZE,
                        onChange: (page, pageSize) => {
                            console.log(page, pageSize);
                            this.loadData(page);
                        }
                    }
                }
                columns={this.columns}
            />
        )

    }

    componentDidMount() {
        this.setState({
            loading: true
        });
        this.loadData(1);
    }

    loadData = (pageIndex) => {
        this.pageIndex = pageIndex;
        api.userList({pageIndex, pageSize: PAGE_SIZE})
            .then(res => res.json())
            .then(result => {
                // {"code":0,"message":"SUCCESS.","data":{"total":3,"list":[{"u
                const {code, message, data: {list, total} = {}} = result;
                this.setState({
                    loading: false,
                    data: list,
                    total: total
                });
            })
            .catch(e => {
                this.setState({
                    loading: false
                });
                console.log(e);
            })
    };
    toggleForbid = (record) => {
        const forbid = record.forbid === '1' ? 0 : 1;
        api.updateUser({forbid})(record.uid)
            .then(res => res.json())
            .then(result => {
                this.loadData(this.pageIndex);
            }).catch(e => {
            console.log(e);
        });
    }
}
