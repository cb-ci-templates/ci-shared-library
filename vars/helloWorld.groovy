// vars/helloWorld.groovy
def call(name) {
    // you can call any valid step functions from your code, just like you can from Pipeline scripts
    echo "Hello ${name}"
    echo "Have a great day!"
    //sh "env | sort"
}
