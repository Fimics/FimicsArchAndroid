import React from 'react';
import './Index.less'
import {Table, Popconfirm} from 'antd';
import api from '../../service';

const PAGE_SIZE = 10;

export default class Index extends React.Component {
    state = {
        data: [],
        total: 0,
        loading: false
    };

    constructor(props) {
        super(props);
        // "categoryId": 1,
        //     "categoryName": "热门",
        //     "createTime": "2022-09-10 15:53:48"
        this.columns = [
            {
                title: '商品ID',
                dataIndex: 'categoryId',
                width: '20%'
            }
            , {
                title: '类别名称',
                dataIndex: 'categoryName'
            }
            , {
                title: '创建时间',
                dataIndex: 'createTime'
            },
            {
                title: "operation",
                dataIndex: 'operation',
                render: (text, record) => {
                    return <Popconfirm
                        title={`确定要删除${record.categoryName}?`}
                        onConfirm={() => this.removeCategory(record)}
                    >
                        <a>删除</a>
                    </Popconfirm>
                },
                width: '20%'
            }

        ];
    }

    render() {
        const {data, total, loading} = this.state;
        return <Table
            columns={this.columns}
            rowKey={item => item.categoryId}
            dataSource={data}
            pagination={
                {
                    total,
                    pageSize: PAGE_SIZE,
                    onChange: (page, pageSize) => {
                        this.loadData(page);
                    }
                }
            }
            loading={loading}
        />
    }

    componentDidMount() {
        this.setState({
            loading: true
        });
        this.loadData(1);
    }

    loadData = pageIndex => {
        this.pageIndex = pageIndex;
        api.categoryList({pageIndex, pageSize: PAGE_SIZE})
            .then(res => res.json())
            .then(result => {
                const {data: {list, total} = {}} = result;
                this.setState(
                    {
                        loading: false,
                        data: list,
                        total: total
                    }
                )
            })
            .catch(e => {
                console.log(e);
                this.setState({
                    loading: false
                })
            });

    };
    removeCategory = record => {
        api.removeCategory(record.categoryId)
            .then(res => res.json())
            .then(result => {
                this.loadData(this.pageIndex);
            })
            .catch(e => {
                console.log(e);
            });
    };

}
