module.exports = {
    mode: "development",
    resolve: {
        extensions: [".js", ".ts", ".tsx"]
    },
    devServer: {
        historyApiFallback: true,
        port: 9000,
        proxy: {
            '/api': {
                target: {
                   host: "0.0.0.0",
                   protocol: 'http:',
                   port: 8080
                },
                pathRewrite: {
                   '^/api': ''
                },
            },        
        }
    },
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                use: 'ts-loader',
                exclude: /node_modules/
            },
            {
                test: /\.css$/,
                use: ["style-loader","css-loader"],
            }
        ]  
    }
}
    
